package com.betadevels.onlineshopping.action.order;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.CustomerHibernateDAO;
import com.betadevels.onlineshopping.db.hibernate.OrderHibernateDAO;
import com.betadevels.onlineshopping.exceptions.NotFoundException;
import com.betadevels.onlineshopping.models.Customer;
import com.betadevels.onlineshopping.models.Order;
import com.betadevels.onlineshopping.payload.response.order.AllOrdersResponse;
import com.betadevels.onlineshopping.payload.response.order.OrderResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GetAllOrdersAction implements Action<AllOrdersResponse> {
    private HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private String requestURL;
    private int page, size;
    private Long customerId;

    @Inject
    public GetAllOrdersAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public GetAllOrdersAction withRequestURL(String requestURL)
    {
        this.requestURL = requestURL;
        return this;
    }

    public GetAllOrdersAction withPaginateDetails(int page, int size)
    {
        this.page = page;
        this.size = size;
        return this;
    }

    private String createLink(int page, int size)
    {
        return requestURL + "?page=" + page + "&size=" + size;
    }

    private Map<String, String> preparePaginationLinks(Long totalCount)
    {
        Map<String, String> linksMap = new HashMap<>();
        int pages = Math.toIntExact((totalCount / this.size) + (totalCount % this.size == 0 ? 0 : 1));
        linksMap.put("first", createLink(1, this.size));
        linksMap.put("prev", this.page > 1 ? createLink(this.page - 1, this.size) : null);
        linksMap.put("next", this.page * this.size < totalCount ? createLink( this.page + 1, this.size ) : null);
        linksMap.put("last", createLink(pages, size));

        return linksMap;
    }
    public GetAllOrdersAction forCustomerId(Long customerId )
    {
        this.customerId = customerId;
        return this;
    }


    @Override
    public AllOrdersResponse invoke()
    {
        OrderHibernateDAO orderHibernateDAO = this.hibernateUtil.getOrderHibernateDAO();
        CustomerHibernateDAO customerHibernateDAO = this.hibernateUtil.getCustomerHibernateDAO();

        Optional<Customer> customerOptional = customerHibernateDAO.findById(customerId);
        if( customerOptional.isPresent() )
        {
            Customer customer = customerOptional.get();
            List<Order> orders = orderHibernateDAO.getAllOrders(customer);
            List<OrderResponse> orderResponses = orders.stream().map(order -> modelMapper.map(order, OrderResponse.class)).collect(Collectors.toList());

            AllOrdersResponse ordersResponse = new AllOrdersResponse();
            ordersResponse.setOrders(orderResponses);
            return ordersResponse;
        }
        throw new NotFoundException(Collections.singletonList("No customer matching the given customer_id"));
    }
}


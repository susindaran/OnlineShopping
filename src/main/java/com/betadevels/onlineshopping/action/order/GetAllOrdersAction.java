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
import java.util.List;
import java.util.stream.Collectors;

import static com.betadevels.onlineshopping.util.Utility.preparePaginationLinks;

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

    public GetAllOrdersAction forCustomerId(Long customerId )
    {
        this.customerId = customerId;
        return this;
    }


    @Override
    public AllOrdersResponse invoke()
    {
    	//TODO: Validate pagination parameters
        OrderHibernateDAO orderHibernateDAO = this.hibernateUtil.getOrderHibernateDAO();
        CustomerHibernateDAO customerHibernateDAO = this.hibernateUtil.getCustomerHibernateDAO();

        Optional<Customer> customerOptional = customerHibernateDAO.findById(customerId);
        if( customerOptional.isPresent() )
        {
            Customer customer = customerOptional.get();
            List<Order> orders = orderHibernateDAO.getOrdersOfCustomer( customer, page, size );
            List<OrderResponse> orderResponses = orders.stream().map(order -> modelMapper.map(order, OrderResponse.class)).collect(Collectors.toList());
	        Long totalCount = orderHibernateDAO.countOfCustomer( customer );

	        return AllOrdersResponse.builder()
                                    .count( orders.size() )
                                    .totalCount( totalCount )
                                    .links( preparePaginationLinks( totalCount, size, page, requestURL ) )
                                    .orders( orderResponses )
                                    .build();
        }
        throw new NotFoundException(Collections.singletonList("No customer matching the given customer_id"));
    }
}


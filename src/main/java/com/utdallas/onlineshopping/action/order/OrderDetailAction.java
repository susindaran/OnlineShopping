package com.utdallas.onlineshopping.action.order;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.OrderDetailHibernateDAO;
import com.utdallas.onlineshopping.db.hibernate.OrderHibernateDAO;
import com.utdallas.onlineshopping.exceptions.NotFoundException;
import com.utdallas.onlineshopping.models.Order;
import com.utdallas.onlineshopping.models.OrderDetail;
import com.utdallas.onlineshopping.payload.response.order.OrderResponse;
import com.utdallas.onlineshopping.payload.response.orderdetail.OrderDetailListResponse;
import com.utdallas.onlineshopping.payload.response.orderdetail.OrderDetailResponse;
import com.utdallas.onlineshopping.util.HibernateUtil;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderDetailAction implements Action<OrderDetailListResponse>
{
    private final HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private Long orderId;

    @Inject
    public OrderDetailAction(Provider<HibernateUtil> hibernateUtilProvider,
                          ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public OrderDetailAction forOrderId(Long orderId)
    {
        this.orderId = orderId;
        return this;
    }

    @Override
    public OrderDetailListResponse invoke() {
        OrderHibernateDAO orderHibernateDAO = this.hibernateUtil.getOrderHibernateDAO();
        Optional<Order> orderOptional = orderHibernateDAO.findById(orderId);
        List<OrderDetailResponse> orderDetailResponses = new ArrayList<>();
        if (orderOptional.isPresent()) {

            for( OrderDetail orderDetail : orderOptional.get().getOrderDetails() )
            {
                orderDetailResponses.add(modelMapper.map(orderDetail, OrderDetailResponse.class));
            }
            OrderDetailListResponse orderDetailListResponse = new OrderDetailListResponse();
            orderDetailListResponse.setOrderDetails(orderDetailResponses);
            orderDetailListResponse.setOrder(modelMapper.map(orderOptional.get(), OrderResponse.class));

            return orderDetailListResponse;

        }
        throw new NotFoundException(Collections.singletonList("No order matching the given order_id"));

    }

}

package com.betadevels.onlineshopping.action.order;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.OrderHibernateDAO;
import com.betadevels.onlineshopping.exceptions.NotFoundException;
import com.betadevels.onlineshopping.models.Order;
import com.betadevels.onlineshopping.payload.response.order.OrderResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import org.modelmapper.ModelMapper;

import java.util.Collections;

public class GetOrderAction implements Action<OrderResponse>
{
    private final HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private Long orderId;

    @Inject
    public GetOrderAction(Provider<HibernateUtil> hibernateUtilProvider,
                          ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public GetOrderAction forOrderId(Long orderId)
    {
        this.orderId = orderId;
        return this;
    }

    @Override
    public OrderResponse invoke()
    {
        OrderHibernateDAO orderHibernateDAO = this.hibernateUtil.getOrderHibernateDAO();
        Optional<Order> orderOptional = orderHibernateDAO.findById(orderId);
        if( orderOptional.isPresent() )
        {
            return modelMapper.map( orderOptional.get(), OrderResponse.class);
        }
        throw new NotFoundException(Collections.singletonList("No order matching the given order_id"));
    }
}

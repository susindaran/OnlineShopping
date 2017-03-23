package com.utdallas.onlineshopping.action.order;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.OrderHibernateDAO;
import com.utdallas.onlineshopping.exceptions.NotFoundException;
import com.utdallas.onlineshopping.models.Order;
import com.utdallas.onlineshopping.payload.response.order.OrderResponse;
import com.utdallas.onlineshopping.util.HibernateUtil;
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

package com.betadevels.onlineshopping.action.payment;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.OrderHibernateDAO;
import com.betadevels.onlineshopping.db.hibernate.PaymentHibernateDAO;
import com.betadevels.onlineshopping.exceptions.NotFoundException;
import com.betadevels.onlineshopping.models.Order;
import com.betadevels.onlineshopping.models.Payment;
import com.betadevels.onlineshopping.payload.response.order.OrderResponse;
import com.betadevels.onlineshopping.payload.response.payment.PaymentResponse;
import com.betadevels.onlineshopping.payload.response.payment.PaymentsListResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GetPaymentsAction implements Action<PaymentsListResponse>
{
	private final HibernateUtil hibernateUtil;
	private ModelMapper modelMapper;
	private Long orderId;

	@Inject
	public GetPaymentsAction( Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper )
	{
		this.hibernateUtil = hibernateUtilProvider.get();
		this.modelMapper = modelMapper;
	}

	public GetPaymentsAction forOrderId( Long orderId )
	{
		this.orderId = orderId;
		return this;
	}

	@Override
	public PaymentsListResponse invoke()
	{
		OrderHibernateDAO orderHibernateDAO = this.hibernateUtil.getOrderHibernateDAO();
		PaymentHibernateDAO paymentHibernateDAO = this.hibernateUtil.getPaymentHibernateDAO();

		Optional<Order> orderOptional = orderHibernateDAO.findById( orderId );
		if( orderOptional.isPresent() )
		{
			Order order = orderOptional.get();
			List<Payment> payments = paymentHibernateDAO.getPaymentsOfOrder( order );

			PaymentsListResponse response = new PaymentsListResponse();
			response.setPayments( payments.stream()
			                              .map( payment -> modelMapper.map( payment, PaymentResponse.class ))
			                              .collect( Collectors.toList() ) );
			response.setOrder( modelMapper.map( order, OrderResponse.class ) );

			return response;
		}
		throw new NotFoundException( Collections.singletonList("No order matching the given order_id") );
	}
}

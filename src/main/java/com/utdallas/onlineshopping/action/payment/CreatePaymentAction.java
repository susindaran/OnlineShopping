package com.utdallas.onlineshopping.action.payment;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.OrderHibernateDAO;
import com.utdallas.onlineshopping.db.hibernate.PaymentHibernateDAO;
import com.utdallas.onlineshopping.exceptions.NotFoundException;
import com.utdallas.onlineshopping.models.Order;
import com.utdallas.onlineshopping.models.Payment;
import com.utdallas.onlineshopping.payload.request.payment.CreatePaymentRequest;
import com.utdallas.onlineshopping.payload.response.payment.PaymentResponse;
import com.utdallas.onlineshopping.util.HibernateUtil;
import org.modelmapper.ModelMapper;

import java.util.Collections;

/**
 * Created by susindaran on 3/18/17.
 */
public class CreatePaymentAction implements Action<PaymentResponse>
{
	private final HibernateUtil hibernateUtil;
	private ModelMapper modelMapper;
	private CreatePaymentRequest createPaymentRequest;
	private Long orderId;

	@Inject
	public CreatePaymentAction( Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper )
	{
		this.hibernateUtil = hibernateUtilProvider.get();
		this.modelMapper = modelMapper;
	}

	public CreatePaymentAction withRequest(CreatePaymentRequest request)
	{
		this.createPaymentRequest = request;
		return this;
	}

	public CreatePaymentAction forOrderId( Long orderId )
	{
		this.orderId = orderId;
		return this;
	}

	@Override
	public PaymentResponse invoke()
	{
		OrderHibernateDAO orderHibernateDAO = this.hibernateUtil.getOrderHibernateDAO();
		PaymentHibernateDAO paymentHibernateDAO = this.hibernateUtil.getPaymentHibernateDAO();

		Optional<Order> orderOptional = orderHibernateDAO.findById( orderId );
		if( orderOptional.isPresent() )
		{
			Order order = orderOptional.get();
			Payment payment = paymentHibernateDAO.create( Payment.builder()
			                                                     .order( order )
			                                                     .amount( createPaymentRequest.getAmount() )
			                                                     .reason( createPaymentRequest.getReason() )
			                                                     .ref1( createPaymentRequest.getRef1() )
			                                                     .transactionType(
					                                                     createPaymentRequest.getTransactionType()
					                                                                         .getType() )
			                                                     .build() );
			return modelMapper.map( payment, PaymentResponse.class );
		}
		throw new NotFoundException( Collections.singletonList("No order matching the given order_id") );
	}
}

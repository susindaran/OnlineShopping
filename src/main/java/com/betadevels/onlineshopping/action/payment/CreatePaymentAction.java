package com.betadevels.onlineshopping.action.payment;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.OrderHibernateDAO;
import com.betadevels.onlineshopping.db.hibernate.PaymentHibernateDAO;
import com.betadevels.onlineshopping.enumerations.TransactionType;
import com.betadevels.onlineshopping.exceptions.NotFoundException;
import com.betadevels.onlineshopping.models.Order;
import com.betadevels.onlineshopping.models.OrderDetail;
import com.betadevels.onlineshopping.models.Payment;
import com.betadevels.onlineshopping.models.TaxDetails;
import com.betadevels.onlineshopping.payload.request.payment.CreatePaymentRequest;
import com.betadevels.onlineshopping.payload.response.order.OrderResponse;
import com.betadevels.onlineshopping.payload.response.payment.PaymentResponse;
import com.betadevels.onlineshopping.payload.response.payment.PaymentsListResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreatePaymentAction implements Action<PaymentsListResponse>
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
	public PaymentsListResponse invoke()
	{
		OrderHibernateDAO orderHibernateDAO = this.hibernateUtil.getOrderHibernateDAO();
		PaymentHibernateDAO paymentHibernateDAO = this.hibernateUtil.getPaymentHibernateDAO();

		Optional<Order> orderOptional = orderHibernateDAO.findById( orderId );
		if( orderOptional.isPresent() )
		{
			Order order = orderOptional.get();
			List<OrderDetail> orderDetails = order.getOrderDetails();
			List<PaymentResponse> paymentResponses = new ArrayList<>();
			double totalPrice = orderDetails.stream().mapToDouble( orderDetail -> orderDetail.getProduct().getPrice() ).sum();
			orderDetails.forEach( orderDetail -> paymentResponses.add( modelMapper.map( paymentHibernateDAO.create( Payment.builder()
			                                                                                                               .order( order )
			                                                                                                               .amount( orderDetail.getProduct().getPrice() )
			                                                                                                               .reason( "PRICE" )
			                                                                                                               .ref1( orderDetail.getProduct().getProductId() )
			                                                                                                               .transactionType( TransactionType.DEBIT.getType() )
			                                                                                                               .build() ), PaymentResponse.class ) ) );
			TaxDetails taxDetails = order.getShippingAddress().getTaxDetails();
			double totalTax = taxDetails.getTax() * totalPrice / 100;
			paymentResponses.add( modelMapper.map( paymentHibernateDAO.create( Payment.builder()
			                                                                          .order( order )
			                                                                          .amount( totalTax )
			                                                                          .reason( "TAX" )
			                                                                          .transactionType( TransactionType.DEBIT.getType() )
			                                                                          .build() ), PaymentResponse.class ) );
			PaymentsListResponse paymentsListResponse = new PaymentsListResponse();
			paymentsListResponse.setOrder( modelMapper.map( order, OrderResponse.class ) );
			paymentsListResponse.setPayments( paymentResponses );
			return paymentsListResponse;
		}
		throw new NotFoundException( Collections.singletonList("No order matching the given order_id") );
	}
}

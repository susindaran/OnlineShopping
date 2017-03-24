package com.utdallas.onlineshopping.action.payment;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.OrderHibernateDAO;
import com.utdallas.onlineshopping.db.hibernate.PaymentHibernateDAO;
import com.utdallas.onlineshopping.enumerations.TransactionType;
import com.utdallas.onlineshopping.exceptions.NotFoundException;
import com.utdallas.onlineshopping.models.Order;
import com.utdallas.onlineshopping.models.OrderDetail;
import com.utdallas.onlineshopping.models.Payment;
import com.utdallas.onlineshopping.models.TaxDetails;
import com.utdallas.onlineshopping.payload.request.payment.CreatePaymentRequest;
import com.utdallas.onlineshopping.payload.response.order.OrderResponse;
import com.utdallas.onlineshopping.payload.response.payment.PaymentResponse;
import com.utdallas.onlineshopping.payload.response.payment.PaymentsListResponse;
import com.utdallas.onlineshopping.util.HibernateUtil;
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

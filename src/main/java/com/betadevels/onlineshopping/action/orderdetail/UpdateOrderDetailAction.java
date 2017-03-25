package com.betadevels.onlineshopping.action.orderdetail;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.OrderDetailHibernateDAO;
import com.betadevels.onlineshopping.db.hibernate.PaymentHibernateDAO;
import com.betadevels.onlineshopping.enumerations.TransactionType;
import com.betadevels.onlineshopping.exceptions.InternalErrorException;
import com.betadevels.onlineshopping.models.Order;
import com.betadevels.onlineshopping.models.OrderDetail;
import com.betadevels.onlineshopping.models.Payment;
import com.betadevels.onlineshopping.payload.request.orderdetail.UpdateOrderDetailsStatusRequest;
import com.betadevels.onlineshopping.payload.response.orderdetail.AllOrderDetailsResponse;
import com.betadevels.onlineshopping.payload.response.orderdetail.OrderDetailResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.modelmapper.ModelMapper;


import java.util.ArrayList;
import java.util.List;

import static com.betadevels.onlineshopping.enumerations.OrderStatus.*;


@Slf4j
public class UpdateOrderDetailAction implements Action<AllOrderDetailsResponse>
{
    private final HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private UpdateOrderDetailsStatusRequest orderDetailRequest;

    @Inject
    public UpdateOrderDetailAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }


    public UpdateOrderDetailAction withRequest(UpdateOrderDetailsStatusRequest orderDetailRequest )
    {
        this.orderDetailRequest = orderDetailRequest;
        return this;
    }

    @Override
    public AllOrderDetailsResponse invoke()
    {
        OrderDetailHibernateDAO orderDetailHibernateDAO = hibernateUtil.getOrderDetailHibernateDAO();
	    PaymentHibernateDAO paymentHibernateDAO = hibernateUtil.getPaymentHibernateDAO();
	    List<OrderDetail> orderDetails = orderDetailHibernateDAO.getOrderDetailsByIds(orderDetailRequest.getOrderDetailIds() );
        List<OrderDetailResponse> orderDetailResponses = new ArrayList<>();
        try
        {
	        for( OrderDetail orderDetail : orderDetails )
	        {
	        	switch ( orderDetailRequest.getOrderDetailStatus() )
		        {
			        case SHIPPED:
			        	if( orderDetail.getOrderDetailStatus().equals( PENDING.getStatus() ))
				        {
					        orderDetail.setOrderDetailStatus( SHIPPED.getStatus() );
				        }
				        break;
			        case INVOICED:
				        if( orderDetail.getOrderDetailStatus().equals( SHIPPED.getStatus() ) )
				        {
					        orderDetail.setOrderDetailStatus( INVOICED.getStatus() );
				        }
				        break;
			        case RETURN_INITIATED:
				        if( orderDetail.getOrderDetailStatus().equals( INVOICED.getStatus() ) )
				        {
					        orderDetail.setOrderDetailStatus( RETURN_INITIATED.getStatus() );
				        }
				        break;
			        case RETURN_RECEIVED:
				        if( orderDetail.getOrderDetailStatus().equals( RETURN_INITIATED.getStatus() ) )
				        {
					        orderDetail.setOrderDetailStatus( RETURN_RECEIVED.getStatus() );
					        Order order = orderDetail.getOrderObject();
					        Payment payment = paymentHibernateDAO
							        .getPaymentOfOrderDetail( order, orderDetail.getProduct().getProductId(), TransactionType.DEBIT, "PRICE" );
					        double tax = order.getShippingAddress().getTaxDetails().getTax() * payment.getAmount() / 100;
					        Payment returnPayment = Payment.builder()
					                                 .order( order )
					                                 .reason( "RETURN" )
					                                 .amount( payment.getAmount() + tax )
					                                 .ref1( orderDetail.getProduct().getProductId() )
					                                 .transactionType( TransactionType.CREDIT.getType() ).build();
					        paymentHibernateDAO.create( returnPayment );
				        }
				        break;
		        }
		        orderDetailResponses.add( modelMapper.map( orderDetailHibernateDAO.update( orderDetail ), OrderDetailResponse.class ) );
	        }
	        AllOrderDetailsResponse allOrderDetailsResponse = new AllOrderDetailsResponse();
            allOrderDetailsResponse.setOrderDetails( orderDetailResponses );
            return allOrderDetailsResponse;
        }
        catch( HibernateException e )
        {
            log.error(String.valueOf(e.getCause()));
            throw new InternalErrorException(e.getMessage());
        }
    }
}

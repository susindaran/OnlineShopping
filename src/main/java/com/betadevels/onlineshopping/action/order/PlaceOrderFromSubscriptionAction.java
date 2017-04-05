package com.betadevels.onlineshopping.action.order;

import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.OrderDetailHibernateDAO;
import com.betadevels.onlineshopping.db.hibernate.OrderHibernateDAO;
import com.betadevels.onlineshopping.db.hibernate.ShipmentHibernateDAO;
import com.betadevels.onlineshopping.db.hibernate.SubscriptionHibernateDAO;
import com.betadevels.onlineshopping.enumerations.OrderStatus;
import com.betadevels.onlineshopping.enumerations.ShipmentStatus;
import com.betadevels.onlineshopping.exceptions.NotFoundException;
import com.betadevels.onlineshopping.models.Order;
import com.betadevels.onlineshopping.models.OrderDetail;
import com.betadevels.onlineshopping.models.Shipment;
import com.betadevels.onlineshopping.models.Subscription;
import com.betadevels.onlineshopping.payload.response.order.OrderResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.joda.time.LocalDateTime;
import org.modelmapper.ModelMapper;

import java.util.Collections;

public class PlaceOrderFromSubscriptionAction implements Action<OrderResponse>
{
	private final HibernateUtil hibernateUtil;
	private ModelMapper modelMapper;
	private Long subscriptionId;

	@Inject
	public PlaceOrderFromSubscriptionAction( Provider<HibernateUtil> hibernateUtilProvider,
	                                         ModelMapper modelMapper )
	{
		this.hibernateUtil = hibernateUtilProvider.get();
		this.modelMapper = modelMapper;
	}

	public PlaceOrderFromSubscriptionAction forSubsctiptionId(Long subscriptionId)
	{
		this.subscriptionId = subscriptionId;
		return this;
	}

	@Override
	public OrderResponse invoke()
	{
		SubscriptionHibernateDAO subscriptionHibernateDAO = this.hibernateUtil.getSubscriptionHibernateDAO();
		OrderDetailHibernateDAO orderDetailHibernateDAO = this.hibernateUtil.getOrderDetailHibernateDAO();
		ShipmentHibernateDAO shipmentHibernateDAO = this.hibernateUtil.getShipmentHibernateDAO();
		OrderHibernateDAO orderHibernateDAO = this.hibernateUtil.getOrderHibernateDAO();

		Optional<Subscription> subscriptionOptional = subscriptionHibernateDAO.findById( subscriptionId );
		if( !subscriptionOptional.isPresent() )
		{
			throw new NotFoundException( Collections.singletonList("No subscription matching the given subscription_id") );
		}

		Subscription subscription = subscriptionOptional.get();

		Order order = orderHibernateDAO.create(Order.builder()
		                                            .customer(subscription.getCustomer())
		                                            .shippingAddress(subscription.getShippingAddress())
		                                            .billingAddress(subscription.getBillingAddress())
		                                            .orderStatus( OrderStatus.PENDING.getStatus() )
		                                            .createdAt( LocalDateTime.now() )
		                                            .updatedAt(LocalDateTime.now()).build() );

		Shipment shipment = shipmentHibernateDAO.create(Shipment.builder()
		                                                        .order(order)
		                                                        .status( ShipmentStatus.PICKED.getStatus() )
		                                                        .deliveryDueDate(LocalDateTime.now().plusDays(10))
		                                                        .createdAt(LocalDateTime.now())
		                                                        .updatedAt(LocalDateTime.now()).build() );

		orderDetailHibernateDAO.create( OrderDetail.builder()
		                                           .order( order )
		                                           .product( subscription.getProduct() )
		                                           .shipment( shipment )
		                                           .orderDetailStatus( OrderStatus.PENDING.getStatus() )
		                                           .offer( subscription.getOffer() )
		                                           .quantity( subscription.getQuantity() ).build() );

		subscription.setNextDueDate( subscription.getNextDueDate().plusDays( subscription.getFrequencyInDays() ) );
		subscriptionHibernateDAO.update( subscription );

		shipmentHibernateDAO.reload( shipment );
		orderHibernateDAO.reload( order );

		return modelMapper.map( order, OrderResponse.class );
	}
}

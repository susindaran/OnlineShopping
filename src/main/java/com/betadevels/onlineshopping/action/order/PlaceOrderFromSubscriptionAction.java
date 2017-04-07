package com.betadevels.onlineshopping.action.order;

import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.*;
import com.betadevels.onlineshopping.enumerations.OrderStatus;
import com.betadevels.onlineshopping.enumerations.ShipmentStatus;
import com.betadevels.onlineshopping.models.*;
import com.betadevels.onlineshopping.payload.request.order.PlaceOrderFromSubscriptionRequest;
import com.betadevels.onlineshopping.payload.response.order.CreateOrdersFromSubscriptionsResponse;
import com.betadevels.onlineshopping.payload.response.order.OrderResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.joda.time.LocalDateTime;
import org.modelmapper.ModelMapper;

import java.util.*;
import java.util.stream.Collectors;

public class PlaceOrderFromSubscriptionAction implements Action<CreateOrdersFromSubscriptionsResponse>
{
	private final HibernateUtil hibernateUtil;
	private ModelMapper modelMapper;
	private PlaceOrderFromSubscriptionRequest request;

	@Inject
	public PlaceOrderFromSubscriptionAction( Provider<HibernateUtil> hibernateUtilProvider,
	                                         ModelMapper modelMapper )
	{
		this.hibernateUtil = hibernateUtilProvider.get();
		this.modelMapper = modelMapper;
	}

	public PlaceOrderFromSubscriptionAction withRequest( PlaceOrderFromSubscriptionRequest request )
	{
		this.request = request;
		return this;
	}

	@Override
	public CreateOrdersFromSubscriptionsResponse invoke()
	{
		SubscriptionHibernateDAO subscriptionHibernateDAO = this.hibernateUtil.getSubscriptionHibernateDAO();
		ProductHibernateDAO productHibernateDAO = this.hibernateUtil.getProductHibernateDAO();
		OrderDetailHibernateDAO orderDetailHibernateDAO = this.hibernateUtil.getOrderDetailHibernateDAO();
		ShipmentHibernateDAO shipmentHibernateDAO = this.hibernateUtil.getShipmentHibernateDAO();
		OrderHibernateDAO orderHibernateDAO = this.hibernateUtil.getOrderHibernateDAO();

		List<Long> invalidSubscriptionIds = new ArrayList<>(  );
		List<Subscription> stockUnavailable = new ArrayList<>(  );
		List<OrderResponse> orderResponses = new ArrayList<>(  );
		Map<Customer, Map<Address, List<Subscription>>> customerToAddressMap = new HashMap<>(  );

		request.getSubscriptionIds().forEach( subscriptionId -> {
			//TODO: Change this to batch call.
			Optional<Subscription> subscriptionOptional = subscriptionHibernateDAO.findById( subscriptionId );
			if( !subscriptionOptional.isPresent() )
			{
				invalidSubscriptionIds.add( subscriptionId );
			}
			else
			{
				Subscription subscription = subscriptionOptional.get();

				//Considering only those products which have enough available quantity for order creation
				if( subscription.getQuantity() <= subscription.getProduct().getQuantity() )
				{
					Map<Address, List<Subscription>> addressToSubscriptionsMap = customerToAddressMap
							.computeIfAbsent( subscription.getCustomer(), k -> new HashMap<>() );

					List<Subscription> subscriptions = addressToSubscriptionsMap
							.computeIfAbsent( subscription.getShippingAddress(), k -> new ArrayList<>() );

					subscriptions.add( subscription );
				}
				else
				{
					stockUnavailable.add( subscription );
				}
			}
		} );

		customerToAddressMap.forEach( (customer, addressToSubscriptions) ->
				addressToSubscriptions.forEach( ( shippingAddress, subscriptions) -> {
					Order order = orderHibernateDAO.create(Order.builder()
					                                            .customer(customer)
					                                            .shippingAddress(shippingAddress)
					                                            .billingAddress(shippingAddress)
					                                            .orderStatus( OrderStatus.PENDING.getStatus() )
					                                            .createdAt( LocalDateTime.now() )
					                                            .updatedAt(LocalDateTime.now()).build() );

					Shipment shipment = shipmentHibernateDAO.create(Shipment.builder()
					                                                        .order(order)
					                                                        .status( ShipmentStatus.PICKED.getStatus() )
					                                                        .deliveryDueDate(LocalDateTime.now().plusDays(10))
					                                                        .createdAt(LocalDateTime.now())
					                                                        .updatedAt(LocalDateTime.now()).build() );

					subscriptions.forEach( subscription -> {
						orderDetailHibernateDAO.create( OrderDetail.builder()
						                                           .order( order )
						                                           .product( subscription.getProduct() )
						                                           .shipment( shipment )
						                                           .orderDetailStatus( OrderStatus.PENDING.getStatus() )
						                                           .offer( subscription.getOffer() )
						                                           .quantity( subscription.getQuantity() ).build() );

						//Updating the catalogue for the new available quantity
						//Existing available quantity - quantity used for subscription order
						Product product = subscription.getProduct();
						product.setQuantity( product.getQuantity() - subscription.getQuantity() );
						productHibernateDAO.update( product );

						//Updating subscription to set the next due date based on the frequency
						subscription.setNextDueDate( LocalDateTime.now().plusDays( subscription.getFrequencyInDays() ) );
						subscriptionHibernateDAO.update( subscription );
					} );

					shipmentHibernateDAO.reload( shipment );
					orderHibernateDAO.reload( order );

					orderResponses.add( modelMapper.map( order, OrderResponse.class ) );
				} )
        );

		//Updating the next due dates of subscriptions with insufficient stock
		stockUnavailable.forEach( subscription -> {
			subscription.setNextDueDate( LocalDateTime.now().plusDays( 1 ) );
			subscriptionHibernateDAO.update( subscription );
		} );

		return CreateOrdersFromSubscriptionsResponse.builder()
		                                            .invalidSubscriptionsIds( invalidSubscriptionIds )
		                                            .stockUnavailable( stockUnavailable.stream()
		                                                                               .map( Subscription::getSubscriptionId )
		                                                                               .collect( Collectors.toList() ) )
		                                            .orders( orderResponses ).build();
	}
}

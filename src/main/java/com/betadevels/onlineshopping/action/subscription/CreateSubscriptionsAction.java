package com.betadevels.onlineshopping.action.subscription;

import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.CartHibernateDAO;
import com.betadevels.onlineshopping.db.hibernate.SubscriptionHibernateDAO;
import com.betadevels.onlineshopping.enumerations.SubscriptionStatus;
import com.betadevels.onlineshopping.exceptions.BadRequestException;
import com.betadevels.onlineshopping.models.Cart;
import com.betadevels.onlineshopping.models.Subscription;
import com.betadevels.onlineshopping.payload.request.subscription.CreateSubscriptionsRequest;
import com.betadevels.onlineshopping.payload.response.subscription.SubscriptionListResponse;
import com.betadevels.onlineshopping.payload.response.subscription.SubscriptionResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.joda.time.LocalDateTime;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreateSubscriptionsAction implements Action<SubscriptionListResponse>
{
	private final HibernateUtil hibernateUtil;
	private ModelMapper modelMapper;
	private CreateSubscriptionsRequest createSubscriptionsRequest;

	@Inject
	public CreateSubscriptionsAction( Provider<HibernateUtil> hibernateUtilProvider,
	                                  ModelMapper modelMapper )
	{
		this.hibernateUtil = hibernateUtilProvider.get();
		this.modelMapper = modelMapper;
	}

	public CreateSubscriptionsAction withRequest(CreateSubscriptionsRequest createSubscriptionsRequest)
	{
		this.createSubscriptionsRequest = createSubscriptionsRequest;
		return this;
	}

	@Override
	public SubscriptionListResponse invoke()
	{
		CartHibernateDAO cartHibernateDAO = this.hibernateUtil.getCartHibernateDAO();
		SubscriptionHibernateDAO subscriptionHibernateDAO = this.hibernateUtil.getSubscriptionHibernateDAO();

		List<Long> invalidCartIds = new ArrayList<>(  );
		List<SubscriptionResponse> subscriptions = new ArrayList<>(  );
		List<Long> cartIds = createSubscriptionsRequest.getCartIds();
		List<Integer> frequencies = createSubscriptionsRequest.getFrequencies();
		if( cartIds.size() != frequencies.size() )
		{
			throw new BadRequestException( Collections.singletonList( "IDs and Frequencies arrays are not of same length" ) );
		}

		for( int i = 0; i < cartIds.size(); i++)
		{
			//TODO: Change the following call to batch SQL
			Optional<Cart> cartOptional = cartHibernateDAO.findById( cartIds.get( i ) );
			if( !cartOptional.isPresent() )
			{
				invalidCartIds.add( cartIds.get( i ) );
			}
			else
			{
				Cart cart = cartOptional.get();
				LocalDateTime now = LocalDateTime.now();

				Subscription subscription = Subscription.builder()
				                                        .customer( cart.getCustomer() )
				                                        .product( cart.getProduct() )
				                                        .quantity( cart.getQuantity() )
				                                        .frequencyInDays( frequencies.get( i ) )
				                                        .status( SubscriptionStatus.ACTIVE.getStatus() )
				                                        .nextDueDate( now )
				                                        .createdAt( now )
				                                        .updatedAt( now )
				                                        .offer( cart.getOffer() )
				                                        .build();

				Subscription newSubscription = subscriptionHibernateDAO.create( subscription );
				subscriptions.add( modelMapper.map( newSubscription, SubscriptionResponse.class ) );
			}
		}

		SubscriptionListResponse subscriptionListResponse = new SubscriptionListResponse();
		subscriptionListResponse.setInvalidCartIds( invalidCartIds );
		subscriptionListResponse.setSubscriptions( subscriptions );
		return subscriptionListResponse;
	}
}

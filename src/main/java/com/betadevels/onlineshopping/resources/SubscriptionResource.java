package com.betadevels.onlineshopping.resources;

import com.betadevels.onlineshopping.action.subscription.CreateSubscriptionsAction;
import com.betadevels.onlineshopping.payload.request.subscription.CreateSubscriptionsRequest;
import com.betadevels.onlineshopping.payload.response.subscription.SubscriptionListResponse;
import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Provider;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/subscription")
@Produces( MediaType.APPLICATION_JSON)
@Slf4j
public class SubscriptionResource
{
	private final CreateSubscriptionsAction createSubscriptionsAction;

	@Inject
	public SubscriptionResource( Provider<CreateSubscriptionsAction> createSubscriptionsActionProvider )
	{
		this.createSubscriptionsAction = createSubscriptionsActionProvider.get();
	}

	@POST
	@UnitOfWork
	@Timed
	public Response createSubscriptions( @Context HttpHeaders headers, @NotNull @Valid CreateSubscriptionsRequest createSubscriptionsRequest )
	{
		SubscriptionListResponse subscriptionListResponse = this.createSubscriptionsAction.withRequest( createSubscriptionsRequest )
		                                                                .invoke();
		return Response.status( Response.Status.CREATED ).entity( subscriptionListResponse ).build();
	}
}

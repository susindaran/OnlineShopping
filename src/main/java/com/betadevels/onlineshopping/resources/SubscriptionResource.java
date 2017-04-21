package com.betadevels.onlineshopping.resources;

import com.betadevels.onlineshopping.action.subscription.CreateSubscriptionsAction;
import com.betadevels.onlineshopping.action.subscription.DeleteSubscriptionAction;
import com.betadevels.onlineshopping.action.subscription.GetSubscriptionsAction;

import com.betadevels.onlineshopping.action.subscription.SkipDueDateAction;
import com.betadevels.onlineshopping.action.subscription.UpdateSubscriptionAction;

import com.betadevels.onlineshopping.payload.request.subscription.CreateSubscriptionsRequest;
import com.betadevels.onlineshopping.payload.request.subscription.UpdateSubscriptionRequest;
import com.betadevels.onlineshopping.payload.response.subscription.SubscriptionListResponse;
import com.betadevels.onlineshopping.payload.response.subscription.SubscriptionResponse;
import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Provider;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
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
	private final GetSubscriptionsAction getSubscriptionsAction;
	private final DeleteSubscriptionAction deleteSubscriptionAction;
	private final SkipDueDateAction skipDueDateAction;
	private final UpdateSubscriptionAction updateSubscriptionAction;





	@Inject
	public SubscriptionResource( Provider<CreateSubscriptionsAction> createSubscriptionsActionProvider,
								 Provider<GetSubscriptionsAction> getSubscriptionsActionProvider,
								 Provider<SkipDueDateAction> skipDueDateActionProvider,
								 Provider<UpdateSubscriptionAction> updateSubscriptionActionProvider,
								 Provider<DeleteSubscriptionAction> deleteSubscriptionActionProvider)
	{
		this.createSubscriptionsAction = createSubscriptionsActionProvider.get();
		this.getSubscriptionsAction = getSubscriptionsActionProvider.get();
		this.skipDueDateAction = skipDueDateActionProvider.get();
		this.updateSubscriptionAction = updateSubscriptionActionProvider.get();
		this.deleteSubscriptionAction = deleteSubscriptionActionProvider.get();

	}


	@POST
	@UnitOfWork
	@Timed
	public Response createSubscriptions(@Context HttpHeaders headers, @NotNull @Valid CreateSubscriptionsRequest createSubscriptionsRequest )
	{
		SubscriptionListResponse subscriptionListResponse = this.createSubscriptionsAction.withRequest( createSubscriptionsRequest )
		                                                                .invoke();
		return Response.status( Response.Status.CREATED ).entity( subscriptionListResponse ).build();
	}

	@GET
	@Path("/customer/{customer_id}")
	@UnitOfWork
	@Timed
	public Response getAllSubscriptions(@Context HttpHeaders headers, @Context HttpServletRequest request, @NotNull @PathParam("customer_id") Long customerId, @QueryParam("page") int page, @QueryParam("size") int size)
	{
		SubscriptionListResponse subscriptionListResponse = getSubscriptionsAction.forCustomerId(customerId)
		                                                                          .withRequestURL( request.getRequestURL().toString() )
		                                                                          .withPaginateDetails( page, size )
		                                                                          .invoke();
		return Response.status( Response.Status.OK ).entity( subscriptionListResponse ).build();
	}


	@DELETE
	@Path("/{subscription_id}")
	@UnitOfWork
	@Timed
	public Response delete(@Context HttpHeaders headers, @Context HttpServletRequest request, @NotNull @PathParam("subscription_id") Long subscriptionId)
	{

		deleteSubscriptionAction.withSubscriptionId(subscriptionId).invoke();
		return Response.status( Response.Status.OK ).build();
	}


	@POST
	@Path("/skip/{subscription_id}")
	@UnitOfWork
	@Timed
	public Response skipDueDate(@Context HttpHeaders headers, @NotNull @PathParam("subscription_id") Long subscriptionId)
	{
		SubscriptionResponse subscriptionResponse = skipDueDateAction.forSubscriptionId( subscriptionId ).invoke();
		return Response.status(Response.Status.OK).entity(subscriptionResponse).build();
	}

	@PUT
	@Path("/{subscription_id}")
	@UnitOfWork
	@Timed
	public Response updateSubscription(@Context HttpHeaders headers, @NotNull UpdateSubscriptionRequest updateSubscriptionRequest, @NotNull @PathParam("subscription_id") Long subscriptionId)
	{
		SubscriptionResponse subscriptionResponse=updateSubscriptionAction.withRequest(updateSubscriptionRequest).withId(subscriptionId).invoke();
		return Response.status(Response.Status.OK).entity(subscriptionResponse).build();
	}

}

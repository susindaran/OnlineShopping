package com.betadevels.onlineshopping.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.betadevels.onlineshopping.action.payment.CreatePaymentAction;
import com.betadevels.onlineshopping.action.payment.GetPaymentsAction;
import com.betadevels.onlineshopping.payload.request.payment.CreatePaymentRequest;
import com.betadevels.onlineshopping.payload.response.payment.PaymentsListResponse;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/payment")
@Produces( MediaType.APPLICATION_JSON)
@Slf4j
public class PaymentResource
{
	private final CreatePaymentAction createPaymentAction;
	private final GetPaymentsAction getPaymentsAction;

	@Inject
	public PaymentResource( Provider<CreatePaymentAction> createPaymentActionProvider,
	                        Provider<GetPaymentsAction> getPaymentsActionProvider)
	{
		this.createPaymentAction = createPaymentActionProvider.get();
		this.getPaymentsAction = getPaymentsActionProvider.get();
	}

	@POST
	@Path( "/{order_id}" )
	@UnitOfWork
	@Timed
	public Response createPayment( @Context HttpHeaders headers, @NotNull CreatePaymentRequest createPaymentRequest,
	                               @NotNull @PathParam( "order_id" ) Long orderId )
	{
		PaymentsListResponse paymentsListResponse = this.createPaymentAction.withRequest( createPaymentRequest ).forOrderId( orderId ).invoke();
		return Response.status( Response.Status.CREATED ).entity( paymentsListResponse ).build();
	}

	@GET
	@Path( "/{order_id}" )
	@UnitOfWork
	@Timed
	public Response getPayments( @Context HttpHeaders headers, @NotNull @PathParam( "order_id" ) Long orderId )
	{
		PaymentsListResponse paymentsListResponse = this.getPaymentsAction.forOrderId( orderId ).invoke();
		return Response.status( Response.Status.OK ).entity( paymentsListResponse ).build();
	}
}

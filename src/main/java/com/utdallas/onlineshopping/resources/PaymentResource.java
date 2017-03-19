package com.utdallas.onlineshopping.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.payment.CreatePaymentAction;
import com.utdallas.onlineshopping.payload.request.payment.CreatePaymentRequest;
import com.utdallas.onlineshopping.payload.response.payment.PaymentResponse;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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

	@Inject
	public PaymentResource( Provider<CreatePaymentAction> createPaymentActionProvider )
	{
		this.createPaymentAction = createPaymentActionProvider.get();
	}

	@POST
	@Path( "/{order_id}" )
	@UnitOfWork
	@Timed
	public Response createPayment( @Context HttpHeaders headers, @NotNull CreatePaymentRequest createPaymentRequest,
	                               @NotNull @PathParam( "order_id" ) Long orderId)
	{
		PaymentResponse paymentResponse = this.createPaymentAction.withRequest( createPaymentRequest ).forOrderId( orderId )
		                                                 .invoke();
		return Response.status( Response.Status.CREATED ).entity( paymentResponse ).build();
	}
}

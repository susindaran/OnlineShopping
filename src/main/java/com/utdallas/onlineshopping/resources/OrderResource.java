package com.utdallas.onlineshopping.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.order.PlaceOrderFromCartAction;
import com.utdallas.onlineshopping.payload.request.order.PlaceOrderRequest;
import com.utdallas.onlineshopping.payload.response.order.OrderResponse;
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

@Path("/order")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class OrderResource
{
    private final PlaceOrderFromCartAction placeOrderFromCartAction;

    @Inject
    public OrderResource(Provider<PlaceOrderFromCartAction> placeOrderFromCartActionProvider)
    {
        this.placeOrderFromCartAction = placeOrderFromCartActionProvider.get();
    }

    @POST
    @Path("/{customer_id}")
    @UnitOfWork
    @Timed
    public Response placeOrderFromCart(@Context HttpHeaders headers, @NotNull PlaceOrderRequest request, @NotNull @PathParam("customer_id") Long customerId)
    {
        OrderResponse orderResponse = this.placeOrderFromCartAction.withRequest(request).forCustomerId(customerId).invoke();
        return Response.status( Response.Status.CREATED ).entity(orderResponse).build();
    }
}

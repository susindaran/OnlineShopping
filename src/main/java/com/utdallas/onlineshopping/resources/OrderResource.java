package com.utdallas.onlineshopping.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.order.GetAllOrdersAction;
import com.utdallas.onlineshopping.action.order.GetOrderAction;
import com.utdallas.onlineshopping.action.order.PlaceOrderFromCartAction;
import com.utdallas.onlineshopping.payload.request.order.PlaceOrderRequest;
import com.utdallas.onlineshopping.payload.response.order.AllOrdersResponse;
import com.utdallas.onlineshopping.payload.response.order.OrderResponse;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
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
    private final GetOrderAction getOrderAction;
    private final GetAllOrdersAction getAllOrdersAction;

    @Inject
    public OrderResource(Provider<PlaceOrderFromCartAction> placeOrderFromCartActionProvider,
                         Provider<GetOrderAction> getOrderActionProvider, Provider<GetAllOrdersAction> getAllOrdersForCustomerActionProvider)
    {
        this.placeOrderFromCartAction = placeOrderFromCartActionProvider.get();
        this.getOrderAction = getOrderActionProvider.get();
        this.getAllOrdersAction=getAllOrdersForCustomerActionProvider.get();
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

    @GET
    @Path("/{order_id}")
    @UnitOfWork
    @Timed
    public Response getOrder(@Context HttpHeaders headers, @NotNull @PathParam("order_id") Long orderId)
    {
        OrderResponse orderResponse = this.getOrderAction.forOrderId(orderId).invoke();
        return Response.status( Response.Status.OK ).entity( orderResponse ).build();
    }
    @GET
    @Path("/customer/{customer_id}")
    @UnitOfWork
    @Timed
    public Response getAllOrders(@Context HttpHeaders headers, @Context HttpServletRequest request, @NotNull @PathParam("customer_id") Long customerId, @QueryParam("page") int page, @QueryParam("size") int size)
    {
        AllOrdersResponse allOrdersResponse = getAllOrdersAction.withRequestURL(request.getRequestURL().toString()).withPaginateDetails(page, size).forCustomerId(customerId).invoke();
        return Response.status( Response.Status.OK ).entity( allOrdersResponse ).build();
    }

}

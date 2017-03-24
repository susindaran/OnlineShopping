package com.utdallas.onlineshopping.resources;

import com.codahale.metrics.annotation.Timed;
import com.utdallas.onlineshopping.action.order.OrderDetailAction;
import com.utdallas.onlineshopping.payload.response.orderdetail.OrderDetailListResponse;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.orderdetail.GetAllOrderDetailsActions;
import com.utdallas.onlineshopping.action.orderdetail.UpdateOrderDetailAction;
import com.utdallas.onlineshopping.enumerations.OrderStatus;
import com.utdallas.onlineshopping.payload.request.orderdetail.UpdateOrderDetailsStatusRequest;
import com.utdallas.onlineshopping.payload.response.orderdetail.AllOrderDetailsResponse;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/order_detail")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class OrderDetailResource
{
    private final GetAllOrderDetailsActions getAllOrderDetailsActions;
    private final UpdateOrderDetailAction updateOrderDetailAction;
    private OrderDetailAction orderDetailAction;

    @Inject
    public OrderDetailResource( Provider<GetAllOrderDetailsActions> getOrderDetailActionProvider,
                                Provider<UpdateOrderDetailAction> updateOrderDetailActionProvider,
                                Provider<OrderDetailAction> orderDetailActionProvider)
    {
        this.getAllOrderDetailsActions =getOrderDetailActionProvider.get();
        this.updateOrderDetailAction=updateOrderDetailActionProvider.get();
        this.orderDetailAction = orderDetailActionProvider.get();
    }

    @GET
    @Path("/all")
    @UnitOfWork
    @Timed
    public Response getAllOrderDetails(@Context HttpHeaders headers, @Context HttpServletRequest request, @QueryParam("page") int page, @QueryParam("size") int size)
    {
        AllOrderDetailsResponse allOrderDetailsResponse = getAllOrderDetailsActions
                .withRequestURL(request.getRequestURL().toString() )
                .withPaginateDetails(page, size)
                .invoke();
        return Response.status(Response.Status.OK).entity(allOrderDetailsResponse).build();
    }


    @GET
    @Path("/{status}")
    @UnitOfWork
    @Timed
    public Response getAllOrderDetailsByStatus( @Context HttpHeaders headers, @Context HttpServletRequest request, @QueryParam("page") int page, @QueryParam("size") int size, @NotNull @PathParam("status") OrderStatus status )
    {
        AllOrderDetailsResponse allOrderDetailsResponse = getAllOrderDetailsActions
                .withRequestURL(request.getRequestURL().toString() )
                .withPaginateDetails(page, size)
                .withStatus( status.toString() )
                .invoke();
        return Response.status(Response.Status.OK).entity( allOrderDetailsResponse ).build();
    }


    @PUT
    @Path("/status")
    @UnitOfWork
    @Timed
    public Response update(@Context HttpHeaders headers, @NotNull UpdateOrderDetailsStatusRequest updateOrderDetailsStatusRequest )
    {
         AllOrderDetailsResponse allOrderDetailsResponse = this.updateOrderDetailAction.withRequest(
		         updateOrderDetailsStatusRequest ).invoke();
        return Response.status(Response.Status.OK).entity( allOrderDetailsResponse ).build();
    }

    @GET
    @Path("/order/{order_id}")
    @UnitOfWork
    @Timed
    public Response getOrderDetails(@Context HttpHeaders headers, @NotNull @PathParam("order_id") Long orderId)
    {
        OrderDetailListResponse orderDetailResponse = orderDetailAction.forOrderId(orderId).invoke();
        return Response.status( Response.Status.OK ).entity( orderDetailResponse ).build();
    }
}

package com.utdallas.onlineshopping.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.orderDetail.GetOrderDetailAction;
import com.utdallas.onlineshopping.action.orderDetail.UpdateOrderDetailAction;
import com.utdallas.onlineshopping.payload.request.orderDetail.GetOrderDetailRequest;
import com.utdallas.onlineshopping.payload.response.orderdetail.GetAllOrderDetailsResponse;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by vidya on 3/22/17.
 */

@Path("/orderdetails")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class OrderDetailResource {


    private final GetOrderDetailAction getOrderDetailAction;
    private final UpdateOrderDetailAction updateOrderDetailAction;

    @Inject
    public OrderDetailResource(Provider<GetOrderDetailAction> getOrderDetailActionProvider, Provider<UpdateOrderDetailAction> updateOrderDetailActionProvider)
    {
        this.getOrderDetailAction=getOrderDetailActionProvider.get();
        this.updateOrderDetailAction=updateOrderDetailActionProvider.get();

    }

    @GET
    @Path("/")
    @UnitOfWork
    @Timed
    public Response getAllShipment(@Context HttpHeaders headers, @Context HttpServletRequest request, @QueryParam("page") int page, @QueryParam("size") int size)
    {
        GetAllOrderDetailsResponse allOrderDetailsResponse = getOrderDetailAction.withRequestURL(request.getRequestURL().toString())
                .withPaginateDetails(page, size, null)
                .invoke();
        return Response.status(Response.Status.OK).entity(allOrderDetailsResponse).build();
    }


    @GET
    @Path("/{status}")
    @UnitOfWork
    @Timed
    public Response getShipment(@Context HttpHeaders headers, @Context HttpServletRequest request, @QueryParam("page") int page, @QueryParam("size") int size, @NotNull @PathParam("status") String status)
    {
        GetAllOrderDetailsResponse allShipmentsResponse = getOrderDetailAction.withRequestURL(request.getRequestURL().toString())
                .withPaginateDetails(page, size, status)
                .invoke();
        return Response.status(Response.Status.OK).entity(allShipmentsResponse).build();
    }


    @PUT
    @Path("/changeStatus")
    @UnitOfWork
    @Timed
    public Response update(@Context HttpHeaders headers, @NotNull GetOrderDetailRequest getOrderDetailRequest, @QueryParam("page") int page, @QueryParam("size") int size)
    {
         GetAllOrderDetailsResponse  getAllOrderDetailsResponse= this.updateOrderDetailAction.withPaginateDetails(page,size).withRequest(getOrderDetailRequest).invoke();
        return Response.status(Response.Status.OK).entity(getAllOrderDetailsResponse).build();
    }

}

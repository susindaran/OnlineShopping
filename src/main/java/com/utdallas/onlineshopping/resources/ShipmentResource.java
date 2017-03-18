package com.utdallas.onlineshopping.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.shipment.GetShipmentAction;
import com.utdallas.onlineshopping.payload.response.shipment.AllShipmentsResponse;
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
 * Created by prathyusha on 3/18/17.
 */

@Path("/shipments")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class ShipmentResource
{
    private final GetShipmentAction getShipmentAction;

    @Inject
    public ShipmentResource(Provider<GetShipmentAction> getShipmentActionProvider)
    {
        this.getShipmentAction=getShipmentActionProvider.get();
    }


    @GET
    @Path("/")
    @UnitOfWork
    @Timed
    public Response getAllShipment(@Context HttpHeaders headers, @Context HttpServletRequest request, @QueryParam("page") int page, @QueryParam("size") int size, @NotNull @PathParam("status") String status)
    {
        AllShipmentsResponse allShipmentsResponse = getShipmentAction.withRequestURL(request.getRequestURL().toString())
                .withPaginateDetails(page, size)
                .invoke();
        return Response.status(Response.Status.OK).entity(allShipmentsResponse).build();
    }


    @GET
    @Path("/{status}")
    @UnitOfWork
    @Timed
    public Response getShipment(@Context HttpHeaders headers, @Context HttpServletRequest request, @QueryParam("page") int page, @QueryParam("size") int size, @NotNull @PathParam("status") String status)
    {
        AllShipmentsResponse allShipmentsResponse = getShipmentAction.withRequestURL(request.getRequestURL().toString())
                .withPaginateDetails(page, size)
                .invoke();
        return Response.status(Response.Status.OK).entity(allShipmentsResponse).build();
    }




}

package com.utdallas.onlineshopping.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.shipment.GetAllShipmentsAction;
import com.utdallas.onlineshopping.action.shipment.UpdateShipmentStatusAction;
import com.utdallas.onlineshopping.enumerations.ShipmentStatus;
import com.utdallas.onlineshopping.payload.request.shipment.UpdateShipmentsStatusRequest;
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

@Path("/shipment")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class ShipmentResource
{
    private final GetAllShipmentsAction getAllShipmentsAction;
    private final UpdateShipmentStatusAction updateShipmentStatusAction;

    @Inject
    public ShipmentResource(Provider<GetAllShipmentsAction> getShipmentActionProvider,
                            Provider<UpdateShipmentStatusAction> updateShipmentStatusActionProvider)
    {
        this.getAllShipmentsAction =getShipmentActionProvider.get();
        this.updateShipmentStatusAction=updateShipmentStatusActionProvider.get();
    }


    @GET
    @Path("/ALL")
    @UnitOfWork
    @Timed
    public Response getAllShipments( @Context HttpHeaders headers, @Context HttpServletRequest request, @QueryParam("page") int page, @QueryParam("size") int size )
    {
        AllShipmentsResponse allShipmentsResponse = getAllShipmentsAction
                .withRequestURL(request.getRequestURL().toString() )
                .withPaginateDetails(page, size)
                .invoke();
        return Response.status(Response.Status.OK).entity( allShipmentsResponse ).build();
    }


    @GET
    @Path("/{status}")
    @UnitOfWork
    @Timed
    public Response getAllShipmentsByStatus( @Context HttpHeaders headers, @Context HttpServletRequest request, @QueryParam("page") int page, @QueryParam("size") int size, @NotNull @PathParam("status") ShipmentStatus status )
    {
        AllShipmentsResponse allShipmentsResponse = getAllShipmentsAction
                .withRequestURL(request.getRequestURL().toString() )
                .withPaginateDetails(page, size)
                .withStatus( status.getStatus() )
                .invoke();
        return Response.status(Response.Status.OK).entity( allShipmentsResponse ).build();
    }

    @PUT
    @Path("/status")
    @UnitOfWork
    @Timed
    public Response updateStatus( @Context HttpHeaders headers, @NotNull UpdateShipmentsStatusRequest updateShipmentsStatusRequest )
    {
        AllShipmentsResponse allShipmentsResponse = this.updateShipmentStatusAction.withRequest(
                updateShipmentsStatusRequest ).invoke();
        return Response.status(Response.Status.OK).entity( allShipmentsResponse ).build();
    }


}

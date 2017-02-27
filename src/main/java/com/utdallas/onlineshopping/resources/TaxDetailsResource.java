package com.utdallas.onlineshopping.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.taxdetails.GetAllStatesAction;
import com.utdallas.onlineshopping.payload.response.taxdetails.StatesListResponse;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/tax_details")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class TaxDetailsResource
{
    private final GetAllStatesAction getAllStatesAction;

    @Inject
    public TaxDetailsResource(Provider<GetAllStatesAction> getAllStatesActionProvider)
    {
        this.getAllStatesAction = getAllStatesActionProvider.get();
    }

    @GET
    @Path("/state/all")
    @UnitOfWork
    @Timed
    public Response getAllStates(@Context HttpHeaders headers)
    {
        StatesListResponse statesListResponse = this.getAllStatesAction.invoke();
        return Response.status(Response.Status.OK).entity(statesListResponse).build();
    }
}

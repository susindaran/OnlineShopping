package com.betadevels.onlineshopping.resources;

import com.betadevels.onlineshopping.action.taxdetails.GetTaxDetailOfStateAction;
import com.betadevels.onlineshopping.payload.response.taxdetails.TaxDetailResponse;
import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.betadevels.onlineshopping.action.taxdetails.GetAllStatesAction;
import com.betadevels.onlineshopping.payload.response.taxdetails.StatesListResponse;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
    private final GetTaxDetailOfStateAction getTaxDetailOfStateAction;

    @Inject
    public TaxDetailsResource(Provider<GetAllStatesAction> getAllStatesActionProvider,
                              Provider<GetTaxDetailOfStateAction> getTaxDetailOfStateActionProvider)
    {
        this.getAllStatesAction = getAllStatesActionProvider.get();
        this.getTaxDetailOfStateAction = getTaxDetailOfStateActionProvider.get();
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

    @GET
    @Path("/state/{state}")
    @UnitOfWork
    @Timed
    public Response getStateTaxDetails( @Context HttpHeaders headers, @NotNull @NotEmpty @PathParam( "state" ) String state)
    {
        TaxDetailResponse response = this.getTaxDetailOfStateAction.forState( state ).invoke();
        return Response.status( Response.Status.OK ).entity( response ).build();
    }
}

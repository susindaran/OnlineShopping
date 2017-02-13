package com.utdallas.onlineshopping.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.customer.ChallengeLoginAction;
import com.utdallas.onlineshopping.action.customer.CreateCustomerAction;
import com.utdallas.onlineshopping.action.customer.GetCustomerAction;
import com.utdallas.onlineshopping.action.customer.UpdateCustomerAction;
import com.utdallas.onlineshopping.payload.request.customer.ChallengeLoginRequest;
import com.utdallas.onlineshopping.payload.request.customer.CreateCustomerRequest;
import com.utdallas.onlineshopping.payload.request.customer.UpdateCustomerRequest;
import com.utdallas.onlineshopping.payload.response.customer.ChallengeLoginResponse;
import com.utdallas.onlineshopping.payload.response.customer.CustomerResponse;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/customer")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class CustomerResource
{
    private final GetCustomerAction getCustomerAction;
    private final CreateCustomerAction createCustomerAction;
    private final ChallengeLoginAction challengeLoginAction;
    private final UpdateCustomerAction updateCustomerAction;

    @Inject
    public CustomerResource( Provider<GetCustomerAction> getCustomerActionProvider,
                             Provider<CreateCustomerAction> createCustomerActionProvider,
                             Provider<ChallengeLoginAction> challengeLoginActionProvider,
                             Provider<UpdateCustomerAction> updateCustomerActionProvider)
    {
        this.getCustomerAction = getCustomerActionProvider.get();
        this.createCustomerAction = createCustomerActionProvider.get();
        this.challengeLoginAction = challengeLoginActionProvider.get();
        this.updateCustomerAction = updateCustomerActionProvider.get();
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    @Timed
    public Response get(@Context HttpHeaders headers, @PathParam("id") Long id)
    {
        CustomerResponse customerResponse = this.getCustomerAction.withId(id).invoke();
        return Response.ok().entity(customerResponse).build();
    }

    @POST
    @Path("/")
    @UnitOfWork
    @Timed
    public Response create(@Context HttpHeaders headers, @NotNull CreateCustomerRequest createCustomerRequest)
    {
        CustomerResponse customerResponse = this.createCustomerAction.withRequest(createCustomerRequest).invoke();
        return Response.status(Response.Status.CREATED).entity(customerResponse).build();
    }

    @POST
    @Path("/login")
    @UnitOfWork
    @Timed
    public Response challengeLogin(@Context HttpHeaders headers, @NotNull ChallengeLoginRequest challengeLoginRequest)
    {
        ChallengeLoginResponse challengeLoginResponse = this.challengeLoginAction.withRequest(challengeLoginRequest).invoke();
        return Response.status(Response.Status.OK).entity(challengeLoginResponse).build();
    }

    @POST
    @Path("/update/{id}")
    @UnitOfWork
    @Timed
    public Response update(@Context HttpHeaders headers, @NotNull UpdateCustomerRequest updateCustomerRequest, @NotNull @PathParam("id") Long id)
    {
        CustomerResponse customerResponse = this.updateCustomerAction.withId(id).withRequest(updateCustomerRequest).invoke();
        return Response.status(Response.Status.OK).entity(customerResponse).build();
    }
}

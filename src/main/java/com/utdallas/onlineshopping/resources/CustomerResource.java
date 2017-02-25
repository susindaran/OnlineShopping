package com.utdallas.onlineshopping.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.address.AddAddressAction;
import com.utdallas.onlineshopping.action.address.DeleteAddressAction;
import com.utdallas.onlineshopping.action.address.EditAddressAction;
import com.utdallas.onlineshopping.action.card.AddCardDetailAction;
import com.utdallas.onlineshopping.action.card.DeleteCardDetailAction;
import com.utdallas.onlineshopping.action.card.EditCardDetailAction;
import com.utdallas.onlineshopping.action.customer.ChallengeLoginAction;
import com.utdallas.onlineshopping.action.customer.CreateCustomerAction;
import com.utdallas.onlineshopping.action.customer.GetCustomerAction;
import com.utdallas.onlineshopping.action.customer.UpdateCustomerAction;
import com.utdallas.onlineshopping.payload.request.address.AddressRequest;
import com.utdallas.onlineshopping.payload.request.card.CardDetailRequest;
import com.utdallas.onlineshopping.payload.request.customer.ChallengeLoginRequest;
import com.utdallas.onlineshopping.payload.request.customer.CreateCustomerRequest;
import com.utdallas.onlineshopping.payload.request.customer.UpdateCustomerRequest;
import com.utdallas.onlineshopping.payload.response.address.AddressResponse;
import com.utdallas.onlineshopping.payload.response.card.CardDetailResponse;
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
    private final AddAddressAction addAddressAction;
    private final DeleteAddressAction deleteAddressAction;
    private final AddCardDetailAction addCardDetailAction;
    private final DeleteCardDetailAction deleteCardDetailAction;
    private final EditAddressAction editAddressAction;
    private final EditCardDetailAction editCardDetailAction;

    @Inject
    public CustomerResource(Provider<GetCustomerAction> getCustomerActionProvider,
                            Provider<CreateCustomerAction> createCustomerActionProvider,
                            Provider<ChallengeLoginAction> challengeLoginActionProvider,
                            Provider<UpdateCustomerAction> updateCustomerActionProvider,
                            Provider<AddAddressAction> addAddressActionProvider,
                            Provider<AddCardDetailAction> addCardDetailActionProvider,
                            Provider<DeleteAddressAction> deleteAddressActionProvider,
                            Provider<DeleteCardDetailAction> deleteCardDetailActionProvider,
                            Provider<EditAddressAction> editAddressActionProvider,
                            Provider<EditCardDetailAction> editCardDetailActionProvider)
    {
        this.getCustomerAction = getCustomerActionProvider.get();
        this.createCustomerAction = createCustomerActionProvider.get();
        this.challengeLoginAction = challengeLoginActionProvider.get();
        this.updateCustomerAction = updateCustomerActionProvider.get();
        this. addAddressAction = addAddressActionProvider.get();
        this.deleteAddressAction = deleteAddressActionProvider.get();
        this.addCardDetailAction = addCardDetailActionProvider.get();
        this.deleteCardDetailAction = deleteCardDetailActionProvider.get();
        this.editAddressAction = editAddressActionProvider.get();
        this.editCardDetailAction = editCardDetailActionProvider.get();
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

    /*
    * Address APIs
    * */

    @POST
    @Path("/address/{customer_id}")
    @UnitOfWork
    @Timed
    public Response addAddress(@Context HttpHeaders headers, @NotNull AddressRequest addressRequest, @NotNull @PathParam("customer_id") Long customerId)
    {
        AddressResponse addressResponse = this.addAddressAction.withRequest(addressRequest).forCustomerId(customerId).invoke();
        return Response.status(Response.Status.CREATED).entity(addressResponse).build();
    }

    @DELETE
    @Path("/address/{address_id}")
    @UnitOfWork
    @Timed
    public Response deleteAddress(@Context HttpHeaders headers, @NotNull @PathParam("address_id") Long addressId)
    {
        this.deleteAddressAction.withAddressId( addressId ).invoke();
        return Response.status(Response.Status.NO_CONTENT).build();
    }


    @PUT
    @Path("/address/{address_id}")
    @UnitOfWork
    @Timed
    public Response editAddress(@Context HttpHeaders headers, @NotNull AddressRequest addressRequest, @NotNull @PathParam("address_id") Long addressId)
    {
        AddressResponse addressResponse = this.editAddressAction.forAddressId(addressId).withRequest(addressRequest).invoke();
        return Response.status(Response.Status.OK).entity( addressResponse ).build();
    }


    /*
    * Card APIs
    * */

    @POST
    @Path("/card/{customer_id}")
    @UnitOfWork
    @Timed
    public Response addCardDetail(@Context HttpHeaders headers, @NotNull CardDetailRequest cardDetailRequest, @NotNull @PathParam("customer_id") Long customerId)
    {
        CardDetailResponse cardDetailResponse = this.addCardDetailAction.withRequest(cardDetailRequest).forCustomerId(customerId).invoke();
        return Response.status(Response.Status.CREATED).entity(cardDetailResponse).build();
    }

    @DELETE
    @Path("/card/{card_number}")
    @UnitOfWork
    @Timed
    public Response deleteCardDetail(@Context HttpHeaders headers, @NotNull @PathParam("card_number") String cardNumber)
    {
        this.deleteCardDetailAction.withCardNumber( cardNumber ).invoke();
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PUT
    @Path("/card/{card_number}")
    @UnitOfWork
    @Timed
    public Response editCardDetail(@Context HttpHeaders headers,@NotNull CardDetailRequest cardDetailRequest, @NotNull @PathParam("card_number") String cardNumber)
    {
        CardDetailResponse cardDetailResponse = this.editCardDetailAction.forCardNumber(cardNumber).withRequest(cardDetailRequest).invoke();
        return Response.status(Response.Status.OK).entity( cardDetailResponse ).build();
    }
}

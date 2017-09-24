package com.betadevels.onlineshopping.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.betadevels.onlineshopping.action.address.AddAddressAction;
import com.betadevels.onlineshopping.action.address.DeleteAddressAction;
import com.betadevels.onlineshopping.action.address.EditAddressAction;
import com.betadevels.onlineshopping.action.card.AddCardDetailAction;
import com.betadevels.onlineshopping.action.card.DeleteCardDetailAction;
import com.betadevels.onlineshopping.action.customer.*;
import com.betadevels.onlineshopping.action.card.EditCardDetailAction;
import com.betadevels.onlineshopping.action.customer.ChallengeLoginAction;
import com.betadevels.onlineshopping.action.customer.CreateCustomerAction;
import com.betadevels.onlineshopping.action.customer.GetCustomerAction;
import com.betadevels.onlineshopping.action.customer.UpdateCustomerAction;
import com.betadevels.onlineshopping.payload.request.address.AddressRequest;
import com.betadevels.onlineshopping.payload.request.card.CardDetailRequest;
import com.betadevels.onlineshopping.payload.request.customer.ChallengeLoginRequest;
import com.betadevels.onlineshopping.payload.request.customer.CreateCustomerRequest;
import com.betadevels.onlineshopping.payload.request.customer.UpdateCustomerRequest;
import com.betadevels.onlineshopping.payload.response.address.AddressResponse;
import com.betadevels.onlineshopping.payload.response.card.CardDetailResponse;
import com.betadevels.onlineshopping.payload.response.customer.ChallengeLoginResponse;
import com.betadevels.onlineshopping.payload.response.customer.CustomerResponse;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api( value = "Customer operations",
        basePath = "/customer",
        consumes = MediaType.APPLICATION_JSON,
        produces = MediaType.APPLICATION_JSON,
        description = "Customer, Address and Card CRUD")
@Path("/customer")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class CustomerResource
{
    private final GetCustomerAction getCustomerAction;
    private final CreateCustomerAction createCustomerAction;
    private final ChallengeLoginAction challengeLoginAction;
    private final UpdateCustomerAction updateCustomerAction;
    private final DeleteCustomerAction deleteCustomerAction;
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
                            Provider<DeleteCustomerAction> deleteCustomerActionProvider,
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
        this.deleteCustomerAction = deleteCustomerActionProvider.get();
        this. addAddressAction = addAddressActionProvider.get();
        this.deleteAddressAction = deleteAddressActionProvider.get();
        this.addCardDetailAction = addCardDetailActionProvider.get();
        this.deleteCardDetailAction = deleteCardDetailActionProvider.get();
        this.editAddressAction = editAddressActionProvider.get();
        this.editCardDetailAction = editCardDetailActionProvider.get();
    }

    @ApiOperation
    (
            value = "Get customer by id",
            response = CustomerResponse.class
    )
    @GET
    @Path("/{id}")
    @UnitOfWork
    @Timed
    public Response get(@Context HttpHeaders headers, @PathParam("id") Long id)
    {
        CustomerResponse customerResponse = this.getCustomerAction.withId(id).invoke();
        return Response.ok().entity(customerResponse).build();
    }

    @ApiOperation
    (
            value = "Create new customer",
            response = CustomerResponse.class
    )
    @POST
    @UnitOfWork
    @Timed
    public Response create(@Context HttpHeaders headers, @NotNull CreateCustomerRequest createCustomerRequest, @QueryParam("admin") String admin)
    {
        CustomerResponse customerResponse = this.createCustomerAction.withRequest(createCustomerRequest).isAdmin(Boolean.parseBoolean(admin)).invoke();
        return Response.status(Response.Status.CREATED).entity(customerResponse).build();
    }

    @ApiOperation
    (
            value = "Authenticate customer login",
            response = ChallengeLoginResponse.class
    )
    @POST
    @Path("/login")
    @UnitOfWork
    @Timed
    public Response challengeLogin(@Context HttpHeaders headers, @NotNull ChallengeLoginRequest challengeLoginRequest, @QueryParam("admin") String admin)
    {
        ChallengeLoginResponse challengeLoginResponse = this.challengeLoginAction.withRequest(challengeLoginRequest).isAdmin(Boolean.parseBoolean(admin)).invoke();
        return Response.status(Response.Status.OK).entity(challengeLoginResponse).build();
    }

    @ApiOperation
    (
            value = "Update customer profile by id",
            response = CustomerResponse.class
    )
    @PUT
    @Path("/{id}")
    @UnitOfWork
    @Timed
    public Response update(@Context HttpHeaders headers, @NotNull UpdateCustomerRequest updateCustomerRequest, @NotNull @PathParam("id") Long id)
    {
        CustomerResponse customerResponse = this.updateCustomerAction.withId(id).withRequest(updateCustomerRequest).invoke();
        return Response.status(Response.Status.OK).entity(customerResponse).build();
    }

    @ApiOperation
    (
            value = "Delete customer by id",
            response = Void.class
    )
    @DELETE
    @Path("/{id}")
    @UnitOfWork
    @Timed
    public Response delete(@Context HttpHeaders headers, @NotNull @PathParam("id") Long customerId)
    {
        this.deleteCustomerAction.withCustomerId(customerId).invoke();
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    /*
    * Address APIs
    * */

    @ApiOperation
    (
            value = "Add address for customer",
            response = AddressResponse.class
    )
    @POST
    @Path("/address/{customer_id}")
    @UnitOfWork
    @Timed
    public Response addAddress(@Context HttpHeaders headers, @NotNull @Valid AddressRequest addressRequest, @NotNull @PathParam("customer_id") Long customerId)
    {
        AddressResponse addressResponse = this.addAddressAction.withRequest(addressRequest).forCustomerId(customerId).invoke();
        return Response.status(Response.Status.CREATED).entity(addressResponse).build();
    }

    @ApiOperation
    (
            value = "Delete customer address by ID",
            response = Void.class
    )
    @DELETE
    @Path("/address/{address_id}")
    @UnitOfWork
    @Timed
    public Response deleteAddress(@Context HttpHeaders headers, @NotNull @PathParam("address_id") Long addressId)
    {
        this.deleteAddressAction.withAddressId( addressId ).invoke();
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @ApiOperation
    (
            value = "Edit customer address by ID",
            response = AddressResponse.class
    )
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

    @ApiOperation
    (
            value = "Add card details to customer",
            response = CardDetailResponse.class
    )
    @POST
    @Path("/card/{customer_id}")
    @UnitOfWork
    @Timed
    public Response addCardDetail(@Context HttpHeaders headers, @NotNull CardDetailRequest cardDetailRequest, @NotNull @PathParam("customer_id") Long customerId)
    {
        CardDetailResponse cardDetailResponse = this.addCardDetailAction.withRequest(cardDetailRequest).forCustomerId(customerId).invoke();
        return Response.status(Response.Status.CREATED).entity(cardDetailResponse).build();
    }

    @ApiOperation
    (
            value = "Delete card detail",
            response = Void.class
    )
    @DELETE
    @Path("/card/{card_number}")
    @UnitOfWork
    @Timed
    public Response deleteCardDetail(@Context HttpHeaders headers, @NotNull @PathParam("card_number") String cardNumber)
    {
        this.deleteCardDetailAction.withCardNumber( cardNumber ).invoke();
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @ApiOperation
    (
            value = "Edit card detail",
            response = CardDetailResponse.class
    )
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

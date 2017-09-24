package com.betadevels.onlineshopping.resources;

import com.betadevels.onlineshopping.payload.request.cart.UpdateCartRequest;
import com.betadevels.onlineshopping.payload.response.cart.CartResponse;
import com.betadevels.onlineshopping.action.cart.*;
import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.betadevels.onlineshopping.payload.request.cart.AddProductToCartRequest;
import com.betadevels.onlineshopping.payload.response.cart.AddProductToCartResponse;
import com.betadevels.onlineshopping.payload.response.cart.CartItemsResponse;
import io.dropwizard.hibernate.UnitOfWork;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api( value = "Cart operations",
basePath = "/cart",
consumes = MediaType.APPLICATION_JSON,
produces = MediaType.APPLICATION_JSON,
description = "CRUD ops on items in cart")
@Path("/cart")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class CartResource
{
    private final AddProductToCartAction addProductToCartAction;
    private final GetItemsInCartAction getItemsInCartAction;
    private final GetItemsInCartCountAction getItemsInCartCountAction;
    private final UpdateItemsInCartAction updateItemsInCartAction;
    private final DeleteItemsInCartAction deleteItemsInCartAction;
    private final GetParticularCartItemsAction getParticularCartItemsAction;

    @Inject
    public CartResource(Provider<AddProductToCartAction> addProductToCartActionProvider,
                        Provider<GetItemsInCartAction> getItemsInCartActionProvider,
                        Provider<GetItemsInCartCountAction> getItemsInCartCountActionProvider,
                        Provider<UpdateItemsInCartAction> updateItemsInCartActionProvider,
                        Provider<DeleteItemsInCartAction> deleteItemsInCartActionProvider,
                        Provider<GetParticularCartItemsAction> getParticularCartItemsActionProvider)

    {
        this.addProductToCartAction = addProductToCartActionProvider.get();
        this.getItemsInCartAction = getItemsInCartActionProvider.get();
        this.getItemsInCartCountAction = getItemsInCartCountActionProvider.get();
        this.updateItemsInCartAction = updateItemsInCartActionProvider.get();
        this.deleteItemsInCartAction= deleteItemsInCartActionProvider.get();
        this.getParticularCartItemsAction = getParticularCartItemsActionProvider.get();
    }

    @ApiOperation
    (
            value = "Add product to a user's cart",
            response = AddProductToCartResponse.class
    )
    @POST
    @UnitOfWork
    @Timed
    public Response addProductToCart(@Context HttpHeaders headers, @NotNull AddProductToCartRequest request)
    {
        AddProductToCartResponse addProductToCartResponse = this.addProductToCartAction.withRequest(request ).invoke();
        return Response.status( Response.Status.CREATED ).entity( addProductToCartResponse ).build();
    }

    @ApiOperation
    (
            value = "Get items in a user's cart",
            response = CartItemsResponse.class
    )
    @GET
    @Path("/{customer_id}")
    @UnitOfWork
    @Timed
    public Response getCartItems(@Context HttpHeaders headers, @NotNull @PathParam("customer_id") Long customerId, @QueryParam( "only_count" ) boolean onlyCount)
    {
    	if( !onlyCount )
	    {
		    CartItemsResponse cartItemsResponse = this.getItemsInCartAction.forCustomerId(customerId).invoke();
		    return Response.status( Response.Status.OK ).entity( cartItemsResponse ).build();
	    }
        return Response.status( Response.Status.OK ).entity( this.getItemsInCartCountAction.forCustomerId( customerId ).invoke() ).build();
    }

    @ApiOperation
    (
            value = "Get specific cart items",
            response =  CartItemsResponse.class
    )
    @GET
    @UnitOfWork
    @Timed
    public Response getParticularCartItems( @Context HttpHeaders headers, @NotNull @NotEmpty @QueryParam( "cart_id" )
                                            List<Long> cartIds )
    {
        CartItemsResponse cartItemsResponse = this.getParticularCartItemsAction.forCartIds( cartIds ).invoke();
        return Response.status( Response.Status.OK ).entity( cartItemsResponse ).build();
    }

    @ApiOperation
    (
            value = "Update cart",
            response =  CartResponse.class
    )
    @PUT
    @Path("/{cart_id}")
    @UnitOfWork
    @Timed
    public Response update( @Context HttpHeaders headers, @Valid @NotNull UpdateCartRequest updateCartRequest, @NotNull @PathParam("cart_id") Long cartId )
    {
        CartResponse cartResponse = this.updateItemsInCartAction.forCartId(cartId ).withRequest(updateCartRequest ).invoke();
        return Response.status(Response.Status.OK).entity(cartResponse).build();
    }

    @ApiOperation
    (
            value = "Delete item in a cart",
            response = Void.class
    )
    @DELETE
    @Path("/{cart_id}")
    @UnitOfWork
    @Timed
    public Response delete(@Context HttpHeaders headers, @NotNull @PathParam("cart_id") Long cartId)
    {
        this.deleteItemsInCartAction.withCartId(cartId).invoke();
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}

package com.betadevels.onlineshopping.resources;

import com.betadevels.onlineshopping.action.cart.UpdateItemsInCartAction;
import com.betadevels.onlineshopping.payload.request.cart.UpdateCartRequest;
import com.betadevels.onlineshopping.payload.request.category.CategoryRequest;
import com.betadevels.onlineshopping.payload.response.category.CategoryResponse;
import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.betadevels.onlineshopping.action.cart.AddProductToCartAction;
import com.betadevels.onlineshopping.action.cart.GetItemsInCartAction;
import com.betadevels.onlineshopping.action.cart.GetItemsInCartCountAction;
import com.betadevels.onlineshopping.payload.request.cart.AddProductToCartRequest;
import com.betadevels.onlineshopping.payload.response.cart.AddProductToCartResponse;
import com.betadevels.onlineshopping.payload.response.cart.CartItemsResponse;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.sql.Update;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/cart")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class CartResource
{
    private final AddProductToCartAction addProductToCartAction;
    private final GetItemsInCartAction getItemsInCartAction;
    private final GetItemsInCartCountAction getItemsInCartCountAction;
    private final UpdateItemsInCartAction updateItemsInCartAction;

    @Inject
    public CartResource(Provider<AddProductToCartAction> addProductToCartActionProvider,
                        Provider<GetItemsInCartAction> getItemsInCartActionProvider,
                        Provider<GetItemsInCartCountAction> getItemsInCartCountActionProvider,
                        Provider<UpdateItemsInCartAction> updateItemsInCartActionProvider)
    {
        this.addProductToCartAction = addProductToCartActionProvider.get();
        this.getItemsInCartAction = getItemsInCartActionProvider.get();
        this.getItemsInCartCountAction = getItemsInCartCountActionProvider.get();
        this.updateItemsInCartAction = updateItemsInCartActionProvider.get();
    }

    @POST
    @UnitOfWork
    @Timed
    public Response addProductToCart(@Context HttpHeaders headers, @NotNull AddProductToCartRequest request)
    {
        AddProductToCartResponse addProductToCartResponse = this.addProductToCartAction.withRequest(request ).invoke();
        return Response.status( Response.Status.CREATED ).entity( addProductToCartResponse ).build();
    }

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

    @POST
    @Path("/update/{cart_id}")
    @UnitOfWork
    @Timed
    public Response update(@Context HttpHeaders headers, @NotNull UpdateCartRequest updateCartRequest, @NotNull @PathParam("cart_id") Long id)
    {
        CartItemsResponse cartItemsResponse = this.updateItemsInCartAction.withId(id).withRequest(updateCartRequest).invoke();
        return Response.status(Response.Status.OK).entity(cartItemsResponse).build();
    }
}

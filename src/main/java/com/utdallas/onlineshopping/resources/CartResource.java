package com.utdallas.onlineshopping.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.cart.AddProductToCartAction;
import com.utdallas.onlineshopping.action.cart.GetItemsInCartAction;
import com.utdallas.onlineshopping.payload.request.cart.AddProductToCartRequest;
import com.utdallas.onlineshopping.payload.response.cart.CartItemsResponse;
import com.utdallas.onlineshopping.payload.response.cart.CartResponse;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;

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

    @Inject
    public CartResource(Provider<AddProductToCartAction> addProductToCartActionProvider,
                        Provider<GetItemsInCartAction> getItemsInCartActionProvider)
    {
        this.addProductToCartAction = addProductToCartActionProvider.get();
        this.getItemsInCartAction = getItemsInCartActionProvider.get();
    }

    @POST
    @UnitOfWork
    @Timed
    public Response addProductToCart(@Context HttpHeaders headers, @NotNull AddProductToCartRequest request)
    {
        CartResponse cartResponse = this.addProductToCartAction.withRequest(request).invoke();
        return Response.status( Response.Status.CREATED ).entity( cartResponse ).build();
    }

    @GET
    @Path("/{customer_id}")
    @UnitOfWork
    @Timed
    public Response getCartItem(@Context HttpHeaders headers, @NotNull @PathParam("customer_id") Long customerId)
    {
        CartItemsResponse cartItemsResponse = this.getItemsInCartAction.forCustomerId(customerId).invoke();
        return Response.status( Response.Status.OK ).entity( cartItemsResponse ).build();
    }
}

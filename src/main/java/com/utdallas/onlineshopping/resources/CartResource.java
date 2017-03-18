package com.utdallas.onlineshopping.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.cart.AddProductToCartAction;
import com.utdallas.onlineshopping.payload.request.cart.AddProductToCartRequest;
import com.utdallas.onlineshopping.payload.response.cart.CartResponse;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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

    @Inject
    public CartResource(Provider<AddProductToCartAction> addProductToCartActionProvider)
    {
        this.addProductToCartAction = addProductToCartActionProvider.get();
    }

    @POST
    @UnitOfWork
    @Timed
    public Response addProductToCart(@Context HttpHeaders headers, @NotNull AddProductToCartRequest request)
    {
        CartResponse cartResponse = this.addProductToCartAction.withRequest(request).invoke();
        return Response.status( Response.Status.CREATED ).entity( cartResponse ).build();
    }
}

package com.utdallas.onlineshopping.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.product.AddProductAction;
import com.utdallas.onlineshopping.payload.request.product.AddProductRequest;
import com.utdallas.onlineshopping.payload.response.product.ProductResponse;
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

@Path("/product")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class ProductResource
{
    private final AddProductAction addProductAction;

    @Inject
    public ProductResource(Provider<AddProductAction> addProductActionProvider)
    {
        this.addProductAction = addProductActionProvider.get();
    }

    @Path("/")
    @POST
    @UnitOfWork
    @Timed
    public Response addProduct(@Context HttpHeaders headers, @NotNull AddProductRequest addProductRequest)
    {
        ProductResponse productResponse = addProductAction.withRequest(addProductRequest).invoke();
        return Response.status(Response.Status.CREATED).entity(productResponse).build();
    }
}

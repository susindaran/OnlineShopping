package com.utdallas.onlineshopping.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.product.AddProductAction;
import com.utdallas.onlineshopping.action.product.UpdateProductAction;
import com.utdallas.onlineshopping.payload.request.product.AddProductRequest;
import com.utdallas.onlineshopping.payload.request.product.UpdateProductRequest;
import com.utdallas.onlineshopping.payload.response.product.ProductResponse;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
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
    private final UpdateProductAction updateProductAction;

    @Inject
    public ProductResource(Provider<AddProductAction> addProductActionProvider,Provider<UpdateProductAction> updateProductActionProviderActionProvider)
    {
        this.addProductAction = addProductActionProvider.get();
        this.updateProductAction=updateProductActionProviderActionProvider.get();
    }

    @POST
    @UnitOfWork
    @Timed
    public Response addProduct(@Context HttpHeaders headers, @NotNull AddProductRequest addProductRequest)
    {
        ProductResponse productResponse = addProductAction.withRequest(addProductRequest).invoke();
        return Response.status(Response.Status.CREATED).entity(productResponse).build();
    }

    @PUT
    @Path("/{id}")
    @UnitOfWork
    @Timed
    public Response update(@Context HttpHeaders headers, @NotNull UpdateProductRequest updateProductRequest, @NotNull @PathParam("id") String id)
    {
        ProductResponse productResponse = this.updateProductAction.withId(id).withRequest(updateProductRequest).invoke();
        return Response.status(Response.Status.OK).entity(productResponse).build();
    }


}

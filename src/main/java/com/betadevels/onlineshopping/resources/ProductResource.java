package com.betadevels.onlineshopping.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.betadevels.onlineshopping.action.product.*;
import com.betadevels.onlineshopping.payload.request.product.ProductRequest;
import com.betadevels.onlineshopping.payload.response.product.AllProductsResponse;
import com.betadevels.onlineshopping.payload.response.product.ProductResponse;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
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
    private final DeleteProductAction deleteProductAction;
    private final UpdateProductAction updateProductAction;
    private final GetProductAction getProductAction;
    private final GetAllProductsAction getAllProductsAction;
    private final GetProductCountAction getProductCountAction;

    @Inject
    public ProductResource(Provider<AddProductAction> addProductActionProvider,
                           Provider<UpdateProductAction> updateProductActionProvider,
                           Provider<DeleteProductAction> deleteProductActionProvider,
                           Provider<GetProductAction> getProductActionProvider,
                           Provider<GetAllProductsAction> getAllProductsActionProvider,
                           Provider<GetProductCountAction> getProductCountActionProvider)
    {
        this.addProductAction = addProductActionProvider.get();
        this.updateProductAction = updateProductActionProvider.get();
        this.deleteProductAction = deleteProductActionProvider.get();
        this.getProductAction = getProductActionProvider.get();
        this.getAllProductsAction = getAllProductsActionProvider.get();
        this.getProductCountAction = getProductCountActionProvider.get();
    }

    @GET
    @Path("/{product_id}")
    @UnitOfWork
    @Timed
    public Response getProduct(@Context HttpHeaders headers, @NotNull @PathParam("product_id") String productId)
    {
        ProductResponse productResponse = getProductAction.forProductId(productId).invoke();
        return Response.status(Response.Status.OK).entity(productResponse).build();
    }

    @GET
    @UnitOfWork
    @Timed
    public Response getAllProducts(@Context HttpHeaders headers, @Context HttpServletRequest request, @QueryParam("page") int page, @QueryParam("size") int size, @QueryParam("category_id") String categoryId)
    {
        AllProductsResponse allProductsResponse = getAllProductsAction.withRequestURL(request.getRequestURL().toString())
                .withPaginateDetails(page, size, categoryId)
                .invoke();
        return Response.status(Response.Status.OK).entity(allProductsResponse).build();
    }

    @GET
    @Path("/count")
    @UnitOfWork
    @Timed
    public Response getProductCount(@Context HttpHeaders headers)
    {
        AllProductsResponse allProductsResponse = this.getProductCountAction.invoke();
        return Response.status( Response.Status.OK ).entity( allProductsResponse ).build();
    }

    @POST
    @UnitOfWork
    @Timed
    public Response addProduct(@Context HttpHeaders headers, @NotNull ProductRequest productRequest)
    {
        ProductResponse productResponse = addProductAction.withRequest(productRequest).invoke();
        return Response.status(Response.Status.CREATED).entity(productResponse).build();
    }

    @DELETE
    @Path("/{product_id}")
    @UnitOfWork
    @Timed
    public Response delete(@Context HttpHeaders headers, @NotNull @PathParam("product_id") String productId)
    {
        this.deleteProductAction.withProductId(productId).invoke();
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PUT
    @Path("/{product_id}")
    @UnitOfWork
    @Timed
    public Response update(@Context HttpHeaders headers, @NotNull ProductRequest productRequest, @NotNull @PathParam("product_id") String productId)
    {
        ProductResponse productResponse = this.updateProductAction.withId(productId).withRequest(productRequest).invoke();
        return Response.status(Response.Status.OK).entity(productResponse).build();
    }
}

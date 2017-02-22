package com.utdallas.onlineshopping.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.product.GetCategoriesAction;
import com.utdallas.onlineshopping.action.product.GetCategoryAction;
import com.utdallas.onlineshopping.action.product.UpdateCategoriesAction;
import com.utdallas.onlineshopping.payload.request.product.UpdateCategoryRequest;
import com.utdallas.onlineshopping.payload.response.product.CategoryResponse;
import com.utdallas.onlineshopping.payload.response.product.CategoriesResponse;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/category")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class CategoryResource
{
    private final GetCategoryAction getCategoryAction;
    private final UpdateCategoriesAction updateCategoriesAction;
    private final GetCategoriesAction getCategoriesAction;

    @Inject
    public CategoryResource(Provider<GetCategoryAction> getCategoryActionProvider,
                            Provider<UpdateCategoriesAction> updateCategoriesActionProvider,
                            Provider<GetCategoriesAction> getCategoriesActionProvider)
    {
        this.getCategoryAction = getCategoryActionProvider.get();
        this.updateCategoriesAction = updateCategoriesActionProvider.get();
        this.getCategoriesAction = getCategoriesActionProvider.get();
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    @Timed
    public Response get(@Context HttpHeaders headers, @PathParam("id") String id)
    {
        CategoryResponse categoryResponse = this.getCategoryAction.withId(id).invoke();
        return Response.ok().entity(categoryResponse).build();
    }

    @POST
    @Path("/update/{id}")
    @UnitOfWork
    @Timed
    public Response update(@Context HttpHeaders headers, @NotNull UpdateCategoryRequest updateCategoryRequest, @NotNull @PathParam("id") String id)
    {
        CategoryResponse categoryResponse = this.updateCategoriesAction.withId(id).withRequest(updateCategoryRequest).invoke();
        return Response.status(Response.Status.OK).entity(categoryResponse).build();
    }

    @GET
    @Path("/all")
    @UnitOfWork
    @Timed
    public Response getAllCategories(@Context HttpHeaders headers)
    {
        CategoriesResponse categoriesResponse = this.getCategoriesAction.invoke();
        return Response.status(Response.Status.OK).entity(categoriesResponse).build();
    }
}

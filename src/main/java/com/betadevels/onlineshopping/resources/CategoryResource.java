package com.betadevels.onlineshopping.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.betadevels.onlineshopping.action.category.*;
import com.betadevels.onlineshopping.payload.request.category.CategoryRequest;
import com.betadevels.onlineshopping.payload.response.category.CategoryResponse;
import com.betadevels.onlineshopping.payload.response.category.CategoriesResponse;
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
    private final AddCategoryAction addCategoryAction;
    private final DeleteCategoryAction deleteCategoryAction;

    @Inject
    public CategoryResource(Provider<GetCategoryAction> getCategoryActionProvider,
                            Provider<UpdateCategoriesAction> updateCategoriesActionProvider,
                            Provider<GetCategoriesAction> getCategoriesActionProvider,
                            Provider<AddCategoryAction> addCategoryActionProvider,
                            Provider<DeleteCategoryAction> deleteCategoryActionProvider)
    {
        this.getCategoryAction = getCategoryActionProvider.get();
        this.updateCategoriesAction = updateCategoriesActionProvider.get();
        this.getCategoriesAction = getCategoriesActionProvider.get();
        this.addCategoryAction = addCategoryActionProvider.get();
        this.deleteCategoryAction = deleteCategoryActionProvider.get();
    }


    @POST
    @UnitOfWork
    @Timed
    public Response create(@Context HttpHeaders headers, @NotNull CategoryRequest categoryRequest)
    {
        CategoryResponse categoryResponse = this.addCategoryAction.withRequest(categoryRequest).invoke();
        return Response.status(Response.Status.CREATED).entity(categoryResponse).build();
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
    public Response update(@Context HttpHeaders headers, @NotNull CategoryRequest categoryRequest, @NotNull @PathParam("id") String id)
    {
        CategoryResponse categoryResponse = this.updateCategoriesAction.withId(id).withRequest(categoryRequest).invoke();
        return Response.status(Response.Status.OK).entity(categoryResponse).build();
    }

    @GET
    @Path("/all")
    @UnitOfWork
    @Timed
    public Response getAllCategories(@Context HttpHeaders headers) {
        CategoriesResponse categoriesResponse = this.getCategoriesAction.invoke();
        return Response.status(Response.Status.OK).entity(categoriesResponse).build();
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    @Timed
    public Response deleteCategory(@Context HttpHeaders headers,@NotNull @PathParam("id") String categoryId)

    {
        this.deleteCategoryAction.withCategoryId(categoryId).invoke();
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}



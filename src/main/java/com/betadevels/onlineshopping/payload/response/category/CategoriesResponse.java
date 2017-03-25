package com.betadevels.onlineshopping.payload.response.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.betadevels.onlineshopping.models.Category;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

import java.util.List;

@JsonSnakeCase
@Data
@JsonInclude( JsonInclude.Include.NON_NULL)
public class CategoriesResponse
{
    private List<Category> categories;
}

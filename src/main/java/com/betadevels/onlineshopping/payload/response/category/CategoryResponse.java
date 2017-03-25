package com.betadevels.onlineshopping.payload.response.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

@JsonSnakeCase
@Data
@JsonInclude( JsonInclude.Include.NON_NULL)
public class CategoryResponse
{
    private String categoryId;
    private String categoryName;
}

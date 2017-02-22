package com.utdallas.onlineshopping.payload.request.product;
import io.dropwizard.jackson.JsonSnakeCase;

import lombok.Getter;

@JsonSnakeCase
@Getter
public class UpdateCategoryRequest
{
    private String categoryId;
    private String categoryName;
}

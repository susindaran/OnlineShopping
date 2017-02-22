package com.utdallas.onlineshopping.payload.response.product;

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

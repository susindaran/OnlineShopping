package com.utdallas.onlineshopping.payload.request.category;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Getter;

@JsonSnakeCase
@Getter
public class CategoryRequest {
    private String categoryId;
    private String categoryName;
}

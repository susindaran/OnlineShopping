package com.utdallas.onlineshopping.payload.request.category;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Getter;

/**
 * Created by vidya on 2/24/17.
 */
@JsonSnakeCase
@Getter
public class CategoryRequest {
    private String categoryId;
    private String categoryName;
}

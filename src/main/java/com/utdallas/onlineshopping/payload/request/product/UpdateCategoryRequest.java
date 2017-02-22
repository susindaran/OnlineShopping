package com.utdallas.onlineshopping.payload.request.product;
import io.dropwizard.jackson.JsonSnakeCase;

import lombok.Getter;

/**
 * Created by prathyusha on 2/21/17.
 */
@JsonSnakeCase
@Getter
public class UpdateCategoryRequest {
    private String categoryId;
    private String categoryName;

}

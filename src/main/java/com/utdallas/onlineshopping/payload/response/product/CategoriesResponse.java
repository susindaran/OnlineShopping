package com.utdallas.onlineshopping.payload.response.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.utdallas.onlineshopping.models.Category;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

import javax.ws.rs.Path;
import java.util.List;

/**
 * Created by prathyusha on 2/21/17.
 */
@JsonSnakeCase
@Data
@JsonInclude( JsonInclude.Include.NON_NULL)
public class CategoriesResponse {
    private List<Category> categories;
}

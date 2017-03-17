package com.utdallas.onlineshopping.payload.response.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.utdallas.onlineshopping.models.Product;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@JsonSnakeCase
@Data
@Builder
@JsonInclude( JsonInclude.Include.NON_NULL)
public class AllProductsResponse
{
    Map<String, String> links;
    Integer count;
    Long totalCount;
    List<Product> products;
}

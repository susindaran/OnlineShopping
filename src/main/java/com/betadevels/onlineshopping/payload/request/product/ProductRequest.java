package com.betadevels.onlineshopping.payload.request.product;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Getter;

@JsonSnakeCase
@Getter
public class ProductRequest
{
    private String categoryPrefix;
    private String productName;
    private String productDescription;
    private Integer quantity;
    private Double price;
}

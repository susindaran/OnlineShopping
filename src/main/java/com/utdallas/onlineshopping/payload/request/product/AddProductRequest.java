package com.utdallas.onlineshopping.payload.request.product;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Getter;

@JsonSnakeCase
@Getter
public class AddProductRequest
{
    private String categoryPrefix;
    private String productName;
    private String productDescription;
    private int quantity;
    private double price;
}

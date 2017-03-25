package com.betadevels.onlineshopping.payload.response.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

@JsonSnakeCase
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse
{
    private String productId;
    private String productName;
    private String productDescription;
    private int quantity;
    private double price;
}

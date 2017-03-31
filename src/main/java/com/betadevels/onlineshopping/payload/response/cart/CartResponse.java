package com.betadevels.onlineshopping.payload.response.cart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.betadevels.onlineshopping.models.Offer;
import com.betadevels.onlineshopping.payload.response.product.ProductResponse;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

@JsonSnakeCase
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartResponse
{
    private int cartId;
    @JsonIgnoreProperties(value = {"quantity"})
    private ProductResponse product;
    private int quantity;
    private Offer offer;
}

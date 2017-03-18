package com.utdallas.onlineshopping.payload.response.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.utdallas.onlineshopping.models.Offer;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

@JsonSnakeCase
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartResponse
{
    private int cartId;
    private Long customerId;
    private CartProductResponse product;
    private int quantity;
    private Offer offer;

    @JsonSnakeCase
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class CartProductResponse
    {
        private String productId;
        private String productName;
        private String productDescription;
        @JsonIgnore
        private int quantity;
        private double price;
    }
}

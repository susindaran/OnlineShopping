package com.utdallas.onlineshopping.payload.response.cart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.utdallas.onlineshopping.payload.response.customer.CustomerResponse;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

import java.util.List;

@JsonSnakeCase
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartItemsResponse
{
    private int count;
    @JsonIgnoreProperties(value = {"addresses", "card_details"})
    private CustomerResponse customer;
    private List<CartResponse> cartItems;
}

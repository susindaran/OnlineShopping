package com.betadevels.onlineshopping.payload.response.cart;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

@JsonSnakeCase
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddProductToCartResponse
{
	private Long itemsInCart;
	private CartResponse productAdded;
}

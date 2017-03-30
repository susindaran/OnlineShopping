package com.betadevels.onlineshopping.payload.response.cart;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

/**
 * Created by girijagodbole on 3/30/17.
 */
@JsonSnakeCase
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteProductFromCartResponse
{
    private Long itemsInCart;

}

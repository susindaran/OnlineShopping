package com.betadevels.onlineshopping.payload.request.cart;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by girijagodbole on 3/28/17.
 */
@JsonSnakeCase
@Getter
public class DeleteItemsInCartRequest {

    @NotNull
    private Integer quantity;
    @NotNull
    private int cartId;


}

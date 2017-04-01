package com.betadevels.onlineshopping.payload.request.cart;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by vidya on 3/28/17.
 */
@JsonSnakeCase
@Getter
public class UpdateCartRequest {

    private int cartId;
    @NotNull
    private Integer quantity;
}
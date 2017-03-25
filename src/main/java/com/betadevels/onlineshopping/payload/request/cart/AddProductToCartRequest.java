package com.betadevels.onlineshopping.payload.request.cart;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@JsonSnakeCase
@Getter
public class AddProductToCartRequest
{
    @NotNull
    private Long customerId;
    @NotNull @NotEmpty
    private String productId;
    @NotNull
    private Integer quantity;
    private String couponId;
}

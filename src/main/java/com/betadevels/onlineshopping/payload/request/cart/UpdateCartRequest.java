package com.betadevels.onlineshopping.payload.request.cart;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@JsonSnakeCase
@Getter
public class UpdateCartRequest
{
    @NotNull
    @Range(min = 1)
    private Integer quantity;
}
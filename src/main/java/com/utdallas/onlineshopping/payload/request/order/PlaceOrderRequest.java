package com.utdallas.onlineshopping.payload.request.order;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@JsonSnakeCase
@Getter
public class PlaceOrderRequest
{
    @NotNull
    private Long shippingAddressId;
    @NotNull
    private Long billingAddressId;
}

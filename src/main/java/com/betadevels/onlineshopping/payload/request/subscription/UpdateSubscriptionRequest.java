package com.betadevels.onlineshopping.payload.request.subscription;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

@JsonSnakeCase
@Data
public class UpdateSubscriptionRequest
{
    private Integer quantity;
    private Integer frequencyInDays;
}

package com.betadevels.onlineshopping.payload.request.subscription;

import com.betadevels.onlineshopping.enumerations.SubscriptionStatus;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

@JsonSnakeCase
@Data
public class UpdateSubscriptionRequest
{
    private Integer quantity;
    private Integer frequencyInDays;
    private SubscriptionStatus subscriptionStatus;
}

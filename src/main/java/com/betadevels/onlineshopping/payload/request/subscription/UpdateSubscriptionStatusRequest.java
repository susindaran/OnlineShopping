package com.betadevels.onlineshopping.payload.request.subscription;

import com.betadevels.onlineshopping.enumerations.SubscriptionStatus;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Getter;

import java.util.List;

/**
 * Created by girijagodbole on 4/16/17.
 */
@JsonSnakeCase
@Getter
public class UpdateSubscriptionStatusRequest {
    private List<Long> subscriptionIds;
    private SubscriptionStatus status;
}

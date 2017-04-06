package com.betadevels.onlineshopping.payload.request.order;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Getter;

import java.util.List;

@JsonSnakeCase
@Getter
public class PlaceOrderFromSubscriptionRequest
{
	private List<Long> subscriptionIds;
}

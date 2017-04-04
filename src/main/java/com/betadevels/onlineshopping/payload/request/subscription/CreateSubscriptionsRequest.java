package com.betadevels.onlineshopping.payload.request.subscription;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Getter;

import java.util.List;

@JsonSnakeCase
@Getter
public class CreateSubscriptionsRequest
{
	private List<Long> cartIds;
	private List<Integer> frequencies;
}

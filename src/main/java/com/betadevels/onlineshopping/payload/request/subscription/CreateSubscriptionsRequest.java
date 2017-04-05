package com.betadevels.onlineshopping.payload.request.subscription;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

@JsonSnakeCase
@Getter
public class CreateSubscriptionsRequest
{
	private Long customerId;
	private List<Long> cartIds;
	private List<Integer> frequencies;
	@NotNull
	private Long shippingAddressId;
	@NotNull
	private Long billingAddressId;
}

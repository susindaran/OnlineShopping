package com.betadevels.onlineshopping.payload.response.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@JsonSnakeCase
@Data
@Builder
@JsonInclude( JsonInclude.Include.NON_NULL)
public class CreateOrdersFromSubscriptionsResponse
{
	private List<OrderResponse> orders;
	private List<Long> invalidSubscriptionsIds;
	private List<Long> stockUnavailable;
}

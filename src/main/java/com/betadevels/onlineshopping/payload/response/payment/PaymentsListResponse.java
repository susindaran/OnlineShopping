package com.betadevels.onlineshopping.payload.response.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.betadevels.onlineshopping.payload.response.order.OrderResponse;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

import java.util.List;

@JsonSnakeCase
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentsListResponse
{
	@JsonIgnoreProperties(value = {"customer", "shipping_address", "billing_address", "shipments"})
	private OrderResponse order;
	private List<PaymentResponse> payments;
}

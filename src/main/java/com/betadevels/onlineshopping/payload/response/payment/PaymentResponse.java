package com.betadevels.onlineshopping.payload.response.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.betadevels.onlineshopping.enumerations.TransactionType;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

@JsonSnakeCase
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentResponse
{
	private int paymentId;
	private TransactionType transactionType;
	private Double amount;
	private String reason;
	@JsonProperty("ref_1")
	private String ref1;
}

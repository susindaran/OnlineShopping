package com.betadevels.onlineshopping.payload.request.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.betadevels.onlineshopping.enumerations.TransactionType;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@JsonSnakeCase
@Getter
public class CreatePaymentRequest
{
	@NotNull @NotEmpty
	private TransactionType transactionType;
	@NotNull
	private Double amount;
	private String reason;
	@JsonProperty("ref_1")
	private String ref1;
}

package com.betadevels.onlineshopping.payload.request.payment;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@JsonSnakeCase
@Getter
public class CreatePaymentRequest
{
	@NotNull @NotEmpty
	private String cardNumber;
}

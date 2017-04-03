package com.betadevels.onlineshopping.payload.response.offer;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;
import org.joda.time.LocalDateTime;

@JsonSnakeCase
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OfferResponse
{
	private String couponId;
	private int discount;
	private LocalDateTime expiryDate;
}

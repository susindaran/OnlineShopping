package com.betadevels.onlineshopping.payload.response.taxdetails;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

@JsonSnakeCase
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaxDetailResponse
{
	private String state;
	private Float tax;
}

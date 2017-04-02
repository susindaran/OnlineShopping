package com.betadevels.onlineshopping.payload.response.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@JsonSnakeCase
@Data
@Builder
@JsonInclude( JsonInclude.Include.NON_NULL)
public class AllOrdersResponse
{
  Map<String, String> links;
  Integer count;
  Long totalCount;
  @JsonIgnoreProperties(value = {"customer", "shipping_address", "billing_address"})
  private List<OrderResponse> orders;
}

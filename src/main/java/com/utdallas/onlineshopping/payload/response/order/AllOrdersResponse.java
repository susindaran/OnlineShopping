package com.utdallas.onlineshopping.payload.response.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

import java.util.List;

@JsonSnakeCase
@Data
@JsonInclude( JsonInclude.Include.NON_NULL)
public class AllOrdersResponse {
  @JsonIgnoreProperties(value = {"customer", "shipping_address", "billing_address", "shipments"})
  private List<OrderResponse> orders;
}

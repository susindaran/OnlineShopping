package com.utdallas.onlineshopping.payload.response.orderdetail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.utdallas.onlineshopping.payload.response.order.OrderResponse;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

import java.util.List;

@JsonSnakeCase
@Data
@JsonInclude( JsonInclude.Include.NON_NULL)
public class OrderDetailListResponse
{
    @JsonIgnoreProperties(value = {"shipments", "customer", "created_at", "updated_at", "shipping_address", "billing_address"})
    OrderResponse order;
    List<OrderDetailResponse> orderDetails;
}

package com.utdallas.onlineshopping.payload.response.shipment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.utdallas.onlineshopping.payload.response.order.OrderResponse;
import com.utdallas.onlineshopping.payload.response.orderdetail.OrderDetailResponse;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;
import org.joda.time.LocalDateTime;

import java.util.List;

@JsonSnakeCase
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShipmentResponse
{
    private int shipmentId;
    @JsonIgnoreProperties(value = {"customer", "shipments", "orderDetails", "billingAddress"})
    private OrderResponse order;
    @JsonIgnoreProperties(value = {"shipment"})
    private List<OrderDetailResponse> orderDetails;
    private LocalDateTime deliveryDueDate;
    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

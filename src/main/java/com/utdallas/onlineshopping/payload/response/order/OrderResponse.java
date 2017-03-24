package com.utdallas.onlineshopping.payload.response.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.utdallas.onlineshopping.enumerations.OrderStatus;
import com.utdallas.onlineshopping.models.Address;
import com.utdallas.onlineshopping.payload.response.customer.CustomerResponse;
import com.utdallas.onlineshopping.payload.response.shipment.ShipmentResponse;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;
import org.joda.time.LocalDateTime;

import java.util.List;

@JsonSnakeCase
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse
{
    private int orderId;
    @JsonIgnoreProperties(value = {"addresses", "card_details"})
    private CustomerResponse customer;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Address shippingAddress;
    private Address billingAddress;
    private List<ShipmentResponse> shipments;
}

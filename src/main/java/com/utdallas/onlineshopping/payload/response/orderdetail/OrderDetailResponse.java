package com.utdallas.onlineshopping.payload.response.orderdetail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.utdallas.onlineshopping.enumerations.OrderStatus;
import com.utdallas.onlineshopping.payload.response.product.ProductResponse;
import com.utdallas.onlineshopping.payload.response.shipment.ShipmentResponse;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

@JsonSnakeCase
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetailResponse
{
    @JsonIgnoreProperties(value = {"quantity"})
    private ProductResponse product;
    @JsonIgnoreProperties(value = {"order", "orderDetails"})
    private ShipmentResponse shipment;
    private Integer quantity;
    private OrderStatus orderDetailStatus;
}

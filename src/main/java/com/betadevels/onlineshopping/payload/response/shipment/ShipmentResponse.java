package com.betadevels.onlineshopping.payload.response.shipment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.betadevels.onlineshopping.enumerations.ShipmentStatus;
import com.betadevels.onlineshopping.payload.response.orderdetail.OrderDetailResponse;
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
    private List<OrderDetailResponse> orderDetails;
    private LocalDateTime deliveryDueDate;
    private ShipmentStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

package com.utdallas.onlineshopping.payload.request.shipment;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Created by prathyusha on 3/18/17.
 */

@JsonSnakeCase
@Getter
public class ShipmentRequest
{
    private Long shipmentId;
    private org.joda.time.LocalDateTime deliveryDueDate;
    private String status;
    private org.joda.time.LocalDateTime createdAt;
    private org.joda.time.LocalDateTime updatedAt;
}

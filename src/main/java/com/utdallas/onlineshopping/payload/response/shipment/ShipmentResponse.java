package com.utdallas.onlineshopping.payload.response.shipment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.utdallas.onlineshopping.models.Shipment;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;
import org.joda.time.LocalDateTime;

import java.util.List;

@JsonSnakeCase
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ShipmentResponse
{
    private Long shipmentId;
    private LocalDateTime deliveryDueDate;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

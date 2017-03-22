package com.utdallas.onlineshopping.payload.request.shipment;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by prathyusha on 3/18/17.
 */

@JsonSnakeCase
@Getter
public class ShipmentRequest
{
    private List<Integer> shipmentId;
    private String status;
    private org.joda.time.LocalDateTime updatedAt;
}

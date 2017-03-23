package com.utdallas.onlineshopping.payload.request.shipment;

import com.utdallas.onlineshopping.enumerations.ShipmentStatus;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Getter;

import java.util.List;

/**
 * Created by prathyusha on 3/18/17.
 */

@JsonSnakeCase
@Getter
public class ShipmentRequest
{
    private List<Long> shipmentIds;
    private ShipmentStatus status;
}

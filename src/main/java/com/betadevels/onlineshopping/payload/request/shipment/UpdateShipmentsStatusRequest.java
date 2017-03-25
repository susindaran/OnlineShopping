package com.betadevels.onlineshopping.payload.request.shipment;

import com.betadevels.onlineshopping.enumerations.ShipmentStatus;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Getter;

import java.util.List;

@JsonSnakeCase
@Getter
public class UpdateShipmentsStatusRequest
{
    private List<Long> shipmentIds;
    private ShipmentStatus status;
}

package com.betadevels.onlineshopping.payload.response.shipment;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

import java.util.List;
import java.util.Map;

@JsonSnakeCase
@Data
@JsonInclude( JsonInclude.Include.NON_NULL)
public class AllShipmentsResponse
{
    Map<String, String> links;
    Integer count;
    Long totalCount;
    private List<ShipmentResponse> shipments;
}

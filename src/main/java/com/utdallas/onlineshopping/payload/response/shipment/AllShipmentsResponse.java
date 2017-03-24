package com.utdallas.onlineshopping.payload.response.shipment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.utdallas.onlineshopping.models.Shipment;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @JsonIgnoreProperties(value = {"order_details"})
    private List<ShipmentResponse> shipments;
}

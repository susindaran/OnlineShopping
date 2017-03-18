package com.utdallas.onlineshopping.payload.response.shipment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.utdallas.onlineshopping.models.Shipment;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by prathyusha on 3/18/17.
 */
@JsonSnakeCase
@Data
@Builder
@JsonInclude( JsonInclude.Include.NON_NULL)
public class AllShipmentsResponse
{
    Map<String, String> links;
    Integer count;
    Long totalCount;
    private List<Shipment> shipments;

}

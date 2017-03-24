package com.utdallas.onlineshopping.payload.response.orderdetail;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.utdallas.onlineshopping.models.OrderDetail;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by vidya on 3/22/17.
 */
@JsonSnakeCase
@Data
@JsonInclude( JsonInclude.Include.NON_NULL)
public class  GetAllOrderDetailsResponse
{
        Map<String, String> links;
        Integer count;
        Long totalCount;
        private List<OrderDetailResponse> orderDetailsResponses;
}


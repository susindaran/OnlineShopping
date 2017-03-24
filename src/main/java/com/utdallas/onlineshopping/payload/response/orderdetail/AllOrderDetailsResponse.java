package com.utdallas.onlineshopping.payload.response.orderdetail;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

import java.util.List;
import java.util.Map;

@JsonSnakeCase
@Data
@JsonInclude( JsonInclude.Include.NON_NULL)
public class AllOrderDetailsResponse
{
	Map<String, String> links;
    Integer count;
    Long totalCount;
	List<OrderDetailResponse> orderDetails;
}


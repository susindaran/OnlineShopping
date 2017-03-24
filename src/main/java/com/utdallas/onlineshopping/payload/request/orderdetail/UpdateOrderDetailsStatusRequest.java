package com.utdallas.onlineshopping.payload.request.orderdetail;

import com.utdallas.onlineshopping.enumerations.OrderStatus;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Getter;

import java.util.List;

@JsonSnakeCase
@Getter
public class UpdateOrderDetailsStatusRequest
{
	private List<Long> orderDetailIds;
	private OrderStatus orderDetailStatus;
}
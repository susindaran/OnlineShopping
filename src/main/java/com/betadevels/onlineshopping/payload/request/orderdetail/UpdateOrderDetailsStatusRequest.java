package com.betadevels.onlineshopping.payload.request.orderdetail;

import com.betadevels.onlineshopping.enumerations.OrderStatus;
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
package com.betadevels.onlineshopping.payload.response.orderdetail;

import com.betadevels.onlineshopping.enumerations.OrderStatus;
import com.betadevels.onlineshopping.payload.response.offer.OfferResponse;
import com.betadevels.onlineshopping.payload.response.product.ProductResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

@JsonSnakeCase
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetailResponse
{
    private int orderDetailId;
    @JsonIgnoreProperties(value = {"quantity"})
    private ProductResponse product;
    private Integer quantity;
    private OrderStatus orderDetailStatus;
    private OfferResponse offer;
}

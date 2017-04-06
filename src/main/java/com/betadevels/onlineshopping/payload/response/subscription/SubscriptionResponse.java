package com.betadevels.onlineshopping.payload.response.subscription;

import com.betadevels.onlineshopping.models.Address;
import com.betadevels.onlineshopping.models.Offer;
import com.betadevels.onlineshopping.payload.response.product.ProductResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.joda.time.LocalDateTime;

@JsonSnakeCase
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionResponse
{
	private int subscriptionId;
	@JsonIgnoreProperties(value = {"quantity"})
	private ProductResponse product;
	private Integer quantity;
	private Integer frequencyInDays;
	private String status;
	private LocalDateTime nextDueDate;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Offer offer;
	private Address shippingAddress;
	private Address billingAddress;
}

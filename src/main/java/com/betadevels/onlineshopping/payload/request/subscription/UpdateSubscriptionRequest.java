package com.betadevels.onlineshopping.payload.request.subscription;

import com.betadevels.onlineshopping.models.Address;
import com.betadevels.onlineshopping.models.Offer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;
import org.joda.time.LocalDateTime;

/**
 * Created by prathyusha on 4/13/17.
 */
@JsonSnakeCase
@Data
public class UpdateSubscriptionRequest
{
    private Long subscriptionId;
    private Integer quantity;
    private Integer frequencyInDays;
}

package com.betadevels.onlineshopping.payload.response.card;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;
import org.joda.time.LocalDateTime;

@JsonSnakeCase
@Data
@JsonInclude( JsonInclude.Include.NON_NULL)
public class CardDetailResponse
{
    private String cardNumber;
    private LocalDateTime expiryDate;
    private String nameOnCard;
    private Long customerId;
}

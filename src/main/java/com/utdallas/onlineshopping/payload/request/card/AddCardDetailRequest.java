package com.utdallas.onlineshopping.payload.request.card;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Getter;
import org.joda.time.LocalDateTime;

@JsonSnakeCase
@Getter
public class AddCardDetailRequest
{
    private String cardNumber;
    private LocalDateTime expiryDate;
    private String nameOnCard;
}

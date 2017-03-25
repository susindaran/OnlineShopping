package com.betadevels.onlineshopping.payload.request.card;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Getter;

@JsonSnakeCase
@Getter
public class CardDetailRequest
{
    private String cardNumber;
    private String expiryDate;
    private String nameOnCard;
}

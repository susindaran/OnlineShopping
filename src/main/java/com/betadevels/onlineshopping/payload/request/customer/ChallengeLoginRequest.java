package com.betadevels.onlineshopping.payload.request.customer;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Getter;

@JsonSnakeCase
@Getter
public class ChallengeLoginRequest
{
    private String emailId;
    private String password;
}

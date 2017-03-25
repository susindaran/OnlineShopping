package com.betadevels.onlineshopping.payload.response.customer;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Builder;
import lombok.Data;

@JsonSnakeCase
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChallengeLoginResponse
{
    private Long customerId;
    private String emailId;
    private String message;
}

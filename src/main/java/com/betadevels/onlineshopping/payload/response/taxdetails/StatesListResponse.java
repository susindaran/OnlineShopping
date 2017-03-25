package com.betadevels.onlineshopping.payload.response.taxdetails;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

import java.util.List;

@JsonSnakeCase
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatesListResponse
{
    private List<String> states;
}

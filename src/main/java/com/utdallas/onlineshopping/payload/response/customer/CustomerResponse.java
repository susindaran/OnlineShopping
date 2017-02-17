package com.utdallas.onlineshopping.payload.response.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.utdallas.onlineshopping.models.Address;
import com.utdallas.onlineshopping.models.CardDetail;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

import java.util.List;

@JsonSnakeCase
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerResponse
{
    private Long customerId;
    private String firstName;
    private String lastName;
    private String emailId;

    private List<Address> addresses;
    private List<CardDetail> cardDetails;

    @JsonIgnore
    private String password;
}
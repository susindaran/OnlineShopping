package com.utdallas.onlineshopping.payload.response.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

@JsonSnakeCase
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerResponse
{
    private Long customerId;
    private String firstName;
    private String lastName;
    private String emailId;

    @JsonIgnore
    private String password;
}

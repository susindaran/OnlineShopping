package com.utdallas.onlineshopping.payload.request.address;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Getter;

@JsonSnakeCase
@Getter
public class AddressRequest
{
    private String phone;
    private String street;
    private String city;
    private String state;
    private String country;
    private String zipcode;
    private String type;
    private String name;
}

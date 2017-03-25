package com.betadevels.onlineshopping.payload.request.address;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@JsonSnakeCase
@Getter
public class AddressRequest
{
    @NotNull @NotEmpty
    private String phone;
    @NotNull @NotEmpty
    private String street;
    @NotNull @NotEmpty
    private String city;
    @NotNull @NotEmpty
    private String state;
    @NotNull @NotEmpty
    private String country;
    @NotNull @NotEmpty
    private String zipcode;
    @NotNull @NotEmpty
    private String type;
    @NotNull @NotEmpty
    private String name;
}

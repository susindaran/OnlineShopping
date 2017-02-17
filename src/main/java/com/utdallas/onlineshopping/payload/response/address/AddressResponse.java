package com.utdallas.onlineshopping.payload.response.address;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.utdallas.onlineshopping.models.TaxDetails;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

@JsonSnakeCase
@Data
@JsonInclude( JsonInclude.Include.NON_NULL)
public class AddressResponse
{
    private Long addressId;
    private String phone;
    private String street;
    private String city;
    private TaxDetails taxDetails;
    private String country;
    private String zipcode;
    private String type;
    private Long customerId;
}

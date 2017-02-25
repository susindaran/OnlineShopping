package com.utdallas.onlineshopping.payload.request.product;

import com.utdallas.onlineshopping.models.Product;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Getter;

/**
 * Created by girijagodbole on 2/24/17.
 */
@Getter
@JsonSnakeCase
public class UpdateProductRequest {
    private String categoryPrefix;
    private String productName;
    private String productDescription;
    private int quantity;
    private double price;



}

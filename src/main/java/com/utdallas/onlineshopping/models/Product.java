package com.utdallas.onlineshopping.models;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSnakeCase
public class Product extends BaseModel
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT(10) UNSIGNED ZEROFILL")
    private Long id;
    @Column(name = "product_id", unique = true, nullable = false)
    private String productId;
    private String productName;
    private int quantity;
    private double price;
    private String description;
}

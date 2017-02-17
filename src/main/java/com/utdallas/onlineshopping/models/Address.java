package com.utdallas.onlineshopping.models;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "address")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSnakeCase
public class Address extends BaseModel
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id", columnDefinition = "INT(11) UNSIGNED")
    private Long addressId;

    private String phone;

    private String street;

    private String city;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    @JoinColumn(name = "state", nullable = false)
    private TaxDetails taxDetails;

    private String country;

    private String zipcode;

    private String type;

    @Getter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
}

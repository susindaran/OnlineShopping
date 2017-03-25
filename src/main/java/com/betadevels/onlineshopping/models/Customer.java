package com.betadevels.onlineshopping.models;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSnakeCase
public class Customer extends BaseModel
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id", columnDefinition = "INT(11) UNSIGNED")
    private Long customerId;

    private String firstName;

    private String lastName;

    private String emailId;

    private String password;

    @Column(columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean isAdmin;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<CardDetail> cardDetails = new ArrayList<>();
}

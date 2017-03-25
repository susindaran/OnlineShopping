package com.betadevels.onlineshopping.models;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.*;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "card_detail")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSnakeCase
public class CardDetail extends BaseModel
{
    @Id
    @Column(name = "card_number", unique = true, columnDefinition = "varchar(25)")
    private String cardNumber;

    private LocalDateTime expiryDate;

    private String nameOnCard;

    @Getter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
}

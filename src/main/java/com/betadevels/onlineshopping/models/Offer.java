package com.betadevels.onlineshopping.models;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.*;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "offer")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSnakeCase
public class Offer extends BaseModel
{
    @Id
    @Column(name = "coupon_id", unique = true, columnDefinition = "varchar(15)")
    private String couponId;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false)
    private Product product;

    private int discount;
    private LocalDateTime expiryDate;
}

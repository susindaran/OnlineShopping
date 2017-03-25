package com.betadevels.onlineshopping.models;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.*;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "`order`")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSnakeCase
public class Order extends BaseModel
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", columnDefinition = "INT(11) UNSIGNED")
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    private String orderStatus;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    @JoinColumn(name = "shipping_address_id", nullable = false)
    private Address shippingAddress;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    @JoinColumn(name = "billing_address_id", nullable = false)
    private Address billingAddress;

    @OneToMany(mappedBy = "order")
    List<Shipment> shipments;

    @OneToMany(mappedBy = "order")
    List<OrderDetail> orderDetails;
}

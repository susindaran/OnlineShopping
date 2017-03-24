package com.utdallas.onlineshopping.models;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.*;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "shipment")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSnakeCase
public class Shipment extends BaseModel
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_id", columnDefinition = "INT(11) UNSIGNED")
    private Long shipmentId;

    @Getter(AccessLevel.PRIVATE)
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @OneToMany(mappedBy = "shipment")
    private List<OrderDetail> orderDetails;

    private LocalDateTime deliveryDueDate;
    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order getOrderItem()
    {
        return this.order;
    }
}

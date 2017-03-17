package com.utdallas.onlineshopping.models;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.*;
import org.joda.time.LocalDateTime;

import javax.persistence.*;

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

    private LocalDateTime deliveryDueDate;
    private String status;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}

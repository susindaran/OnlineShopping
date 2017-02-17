package com.utdallas.onlineshopping.models;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tax_details")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSnakeCase
public class TaxDetails extends BaseModel
{
    @Id
    @Column(name = "state", unique = true, columnDefinition = "varchar(15)")
    private String state;

    @Column(name = "tax", columnDefinition = "float(6,3)")
    private Float tax;
}

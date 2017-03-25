package com.betadevels.onlineshopping.models;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.*;

import javax.persistence.*;
import java.util.List;

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

    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "taxDetails", fetch = FetchType.LAZY)
    private List<Address> addresses;
}

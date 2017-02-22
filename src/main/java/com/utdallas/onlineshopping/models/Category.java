package com.utdallas.onlineshopping.models;

/**
 * Created by prathyusha on 2/21/17.
 */
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.*;

import javax.persistence.*;
@Entity
@Table(name = "category")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSnakeCase
public class Category extends BaseModel
{

    @Id
    private String categoryId;
    private String categoryName;

}

package com.project.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "variantproduct")
public class VariantProduct extends GenericEntity{
    @ManyToOne
    @JoinColumn(name = "productId",referencedColumnName = "id")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "colorId",referencedColumnName = "id")
    private Color color;
    @ManyToOne
    @JoinColumn(name = "sizeId",referencedColumnName = "id")
    private Size size;
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}

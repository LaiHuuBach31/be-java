package com.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carts")
public class Cart extends GenericEntity{
    @Column(name = "isCheckout")
    private Boolean isCheckout;
    @Column(name = "quantity")
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name = "productId",referencedColumnName = "id")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "userId",referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "sizeId",referencedColumnName = "id")
    private Size size;
    @ManyToOne
    @JoinColumn(name = "colorId",referencedColumnName = "id")
    private Color color;
//    @OneToMany(mappedBy = "cart")
//    @JsonIgnore
//    private Set<CartVariant> carts;
}

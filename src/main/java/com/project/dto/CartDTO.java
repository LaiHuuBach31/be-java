package com.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.project.model.CartVariant;
import com.project.model.Color;
import com.project.model.Product;
import com.project.model.Size;
import com.project.model.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    @NotNull(message = "Quantity is required")
    private Integer quantity;
    private Product product;
    private User user;
    @NotNull(message = "Product is required")
    private Integer productId;
//    private Set<CartVariant> carts;
    private Size size;
    private Color color;
    private Integer sizeId;
    private Integer colorId;
}

package com.project.dto;

//import com.project.model.CartVariant;
import com.project.model.Product;
import com.project.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartViewDTO {
    private Integer quantity;
    private Product product;
    private User user;
//    private Set<CartVariant> carts;
}

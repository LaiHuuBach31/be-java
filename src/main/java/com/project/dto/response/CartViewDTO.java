package com.project.dto.response;
//import com.project.model.CartVariant;
import com.project.model.Color;
import com.project.model.Product;
import com.project.model.Size;
import com.project.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartViewDTO {
    private Integer id;
    private Integer quantity;
    private Product product;
    private Color color;
    private Size size;
    private User user;
//    private Set<CartVariant> carts;
}

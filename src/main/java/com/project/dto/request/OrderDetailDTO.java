package com.project.dto.request;

import com.project.model.Order;
import com.project.model.Product;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    private Integer id;
    private float price;
    private int quantity;
    private float subtotal;
    private Order order;
    private Product product;
    @NotNull(message = "Order is required")
    private int orderId;
    @NotNull(message = "Product is required")
    private int productId;
}

package com.project.dto.response;

import com.project.model.Order;
import com.project.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailViewDTO {
    private Integer id;
    private float price;
    private int quantity;
    private float subtotal;
    private Order order;
    private Product product;
}

package com.project.dto;

import com.project.model.Color;
import com.project.model.Product;
import com.project.model.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VariantProductViewDTO {
    private Product product;
    private Color color;
    private Size size;
    private Integer quantity;
}

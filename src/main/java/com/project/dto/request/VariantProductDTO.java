package com.project.dto;

import com.project.model.Color;
import com.project.model.Product;
import com.project.model.Size;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VariantProductDTO {
    private Integer id;
    private Product product;
    private Color color;
    private Size size;
    @NotNull(message = "Product is required")
    private Integer productId;
    @NotNull(message = "Color is required")
    private Integer colorId;
    @NotNull(message = "Size is required")
    private Integer sizeId;
    @NotNull(message = "Quantity is required")
    private Integer quantity;
}

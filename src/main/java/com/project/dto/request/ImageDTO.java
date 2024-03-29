package com.project.dto.request;

import com.project.model.Product;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDTO {
    private Integer id;
    @NotEmpty(message = "Image name is required")
    private String name;
    private Product product;
    @NotNull(message = "Product is required")
    private Integer productId;
}

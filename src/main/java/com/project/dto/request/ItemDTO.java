package com.project.dto.request;

import com.project.model.Product;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
    private Integer id;
    @NotEmpty(message = "Item name is required")
    @Size(min = 2, message = "Item name should have at least 2 characters")
    private String name;
    private Integer status;
    private Set<Product> products;
}

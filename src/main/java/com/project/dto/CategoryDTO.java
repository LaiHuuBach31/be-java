package com.project.dto;

import com.project.model.Product;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Integer id;
    @NotEmpty(message = "Category name is required")
    @Size(min = 2, message = "Category name should have at least 2 characters")
    private String name;
    private Integer status;
    private Set<Product> products;
}

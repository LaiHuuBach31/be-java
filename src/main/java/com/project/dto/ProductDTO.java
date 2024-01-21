package com.project.dto;

import com.project.model.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    @NotEmpty(message = "Product name is required")
    private String name;
    @NotNull(message = "Product price is required")
    private Float price;
    private Integer discount;
    @NotEmpty(message = "Product image is required")
    private String image;
    private Integer status;
    private String description;
    private Category category;
    private Item item;
    @NotNull(message = "Category is required")
    private Integer categoryId;
    @NotNull(message = "Item is required")
    private Integer itemId;
    private Set<Image> images;
    private Set<VariantProduct> productAttr;
    private Set<Cart> carts;
}

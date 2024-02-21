package com.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductViewDTO {
    private String name;
    private Float price;
    private Integer discount;
    private String image;
    private Integer status;
    private String description;
    private Category category;
    private Item item;
    @JsonIgnore
    private Set<Image> images;
    @JsonIgnore
    private Set<VariantProduct> productAttr;
//    @JsonIgnore
//    private Set<Cart> carts;
}

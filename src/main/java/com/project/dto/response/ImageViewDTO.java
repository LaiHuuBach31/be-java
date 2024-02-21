package com.project.dto;

import com.project.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageViewDTO {
    private Integer id;
    private String name;
    private Product product;
}

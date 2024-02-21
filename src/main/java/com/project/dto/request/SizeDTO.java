package com.project.dto;

import com.project.model.Size;
import com.project.model.VariantProduct;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SizeDTO {
    private Integer id;
    @NotEmpty(message = "Size name is required")
    private String name;
    private Integer status;
    private Set<VariantProduct> sizeAttr;
    private Set<Size> sizes;
}

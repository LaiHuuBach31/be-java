package com.project.dto.request;

import com.project.model.Color;
import com.project.model.VariantProduct;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColorDTO {
    private Integer id;
    @NotEmpty(message = "Color name is required")
    private String name;
    private Integer status;
    private Set<VariantProduct> colorAttr;
    private Set<Color> colors;
}

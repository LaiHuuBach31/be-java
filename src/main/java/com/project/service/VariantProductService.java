package com.project.service;

import com.project.dto.request.VariantProductDTO;
import com.project.dto.response.VariantProductViewDTO;
import com.project.model.VariantProduct;

import java.util.List;

public interface VariantProductService extends Generic<VariantProductDTO, VariantProductViewDTO>{
    List<VariantProductViewDTO> listVariant(Integer sizeId);
}

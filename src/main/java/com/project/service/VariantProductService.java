package com.project.service;

import com.project.model.VariantProduct;

public interface VariantProductService extends Generic<VariantProduct, Integer>{
    VariantProduct checkAttributes(Integer productId, Integer colorId, Integer sizeId);
}

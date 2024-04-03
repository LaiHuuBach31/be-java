package com.project.service;

import com.project.dto.request.ProductDTO;
import com.project.dto.response.ProductViewDTO;
import com.project.model.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService extends Generic<ProductDTO, ProductViewDTO>{
    Page<ProductViewDTO> getAll(String keyword, Integer categoryId, Float minPrice, Float maxPrice, Integer color, Integer size, Integer pageNo, Integer pageSize, String sortBy, String sortOrder);
    List<ProductViewDTO> getProductDetail(Integer productId);
}

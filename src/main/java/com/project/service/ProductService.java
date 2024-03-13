package com.project.service;

import com.project.dto.request.ProductDTO;
import com.project.dto.response.ProductViewDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService extends Generic<ProductDTO, ProductViewDTO>{
    Page<ProductViewDTO> filter(Integer categoryId, Integer pageNo, Integer pageSize);
}

package com.project.service;

import com.project.dto.request.CartDTO;
import com.project.dto.response.CartViewDTO;

import java.util.List;

public interface CartService extends Generic<CartDTO, CartViewDTO> {
    List<CartViewDTO> getAll();
    CartViewDTO update(Integer id, Integer quantity,Integer sizeId,Integer colorId);
}

package com.project.service;

import com.project.dto.CartViewDTO;
import com.project.dto.request.CartDTO;
import com.project.model.Cart;

import java.util.List;

public interface CartService extends Generic<CartDTO, CartViewDTO> {
    List<CartViewDTO> getAll();
}

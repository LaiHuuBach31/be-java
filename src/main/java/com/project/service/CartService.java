package com.project.service;

import com.project.model.Cart;

public interface CartService extends Generic<Cart, Integer> {
    Cart checkCart(Integer productId, Integer colorId, Integer sizeId);
}

package com.project.service.impl;

import com.project.model.Cart;
import com.project.repository.CartRepository;
import com.project.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @Override
    public List<Cart> getAll() {
        return this.cartRepository.findAll();
    }

    @Override
    public Cart findById(Integer key) {
        return this.cartRepository.findById(key).orElse(null);
    }

    @Override
    public List<Cart> findByName(String name) {
        return null;
    }

    @Override
    public Cart saveOrUpdate(Cart object) {
        return this.cartRepository.save(object);
    }

    @Override
    public boolean existsById(Integer key) {
        return this.cartRepository.existsById(key);
    }

    @Override
    public void delete(Integer key) {
        this.cartRepository.delete(findById(key));
    }

    @Override
    public Page<Cart> pagination(Integer pageNo, Integer pageSize) {
        return null;
    }

    @Override
    public List<Cart> search(String keyWord) {
        return null;
    }

    @Override
    public Page<Cart> search(String keyWord, Integer pageNo, Integer pageSize) {
        return null;
    }

    @Override
    public Cart checkCart(Integer productId, Integer colorId, Integer sizeId) {
        return this.cartRepository.checkCart(productId, colorId, sizeId);
    }
}

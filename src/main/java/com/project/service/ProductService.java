package com.project.service;

import com.project.model.Category;
import com.project.model.Product;

public interface ProductService extends Generic<Product, Integer>{
    void deleteByCategory(Integer categoryId);
}

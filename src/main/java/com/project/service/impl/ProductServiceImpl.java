package com.project.service.impl;

import com.project.model.Product;
import com.project.repository.ProductRepository;
import com.project.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> getAll() {
        return this.productRepository.findAll();
    }

    @Override
    public Product findById(Integer id) {
        return this.productRepository.findById(id).orElse(null);
    }

    @Override
    public List<Product> findByName(String name) {
        return this.productRepository.findByName(name);
    }

    @Override
    public Product saveOrUpdate(Product product) {
        return this.productRepository.save(product);
    }

    @Override
    public boolean existsById(Integer id) {
        return this.productRepository.existsById(id);
    }

    @Override
    public void delete(Integer id) {
        this.productRepository.delete(this.findById(id));
    }

    @Override
    public Page<Product> pagination(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        return this.productRepository.findAll(pageable);
    }

    @Override
    public List<Product> search(String keyWord) {
        return this.productRepository.listByName(keyWord);
    }

    @Override
    public Page<Product> search(String keyWord, Integer pageNo, Integer pageSize) {
        List<Product> list = this.search(keyWord);
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        int start = (int) pageable.getOffset();
        int end = pageable.getOffset() + pageable.getPageSize() > list.size() ? list.size() : (int) (pageable.getOffset() + pageable.getPageSize());
        list = list.subList(start, end);
        return new PageImpl<Product>(list, pageable, list.size());
    }

    @Override
    public void deleteByCategory(Integer categoryId) {
        this.productRepository.deleteByCategory(categoryId);
    }
}

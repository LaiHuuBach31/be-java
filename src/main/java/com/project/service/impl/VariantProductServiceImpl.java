package com.project.service.impl;

import com.project.model.VariantProduct;
import com.project.repository.VariantProductRepository;
import com.project.service.VariantProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VariantProductServiceImpl implements VariantProductService {

    private final VariantProductRepository variantProductRepository;

    @Override
    public List<VariantProduct> getAll() {
        return this.variantProductRepository.findAll();
    }

    @Override
    public VariantProduct findById(Integer id) {
        return this.variantProductRepository.findById(id).orElse(null);
    }

    @Override
    public List<VariantProduct> findByName(String name) {
        return null;
    }


    @Override
    public VariantProduct saveOrUpdate(VariantProduct variantProduct) {
        return this.variantProductRepository.save(variantProduct);
    }

    @Override
    public boolean existsById(Integer id) {
        return this.variantProductRepository.existsById(id);
    }

    @Override
    public void delete(Integer id) {
        this.variantProductRepository.delete(this.findById(id));
    }

    @Override
    public Page<VariantProduct> pagination(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        return this.variantProductRepository.findAll(pageable);
    }

    @Override
    public List<VariantProduct> search(String keyWord) {
        return null;
    }

    @Override
    public Page<VariantProduct> search(String keyWord, Integer pageNo, Integer pageSize) {
        return null;
    }

    @Override
    public VariantProduct checkAttributes(Integer productId, Integer colorId, Integer sizeId) {
        return this.variantProductRepository.checkAttributes(productId, colorId, sizeId);
    }
}

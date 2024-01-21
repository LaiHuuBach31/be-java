//package com.project.service;
//
//import com.project.model.CartVariant;
//import com.project.repository.CartVariantRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class CartVariantServiceImpl implements CartVariantService{
//
//    private final CartVariantRepository cartVariantRepository;
//
//    @Override
//    public List<CartVariant> getAll() {
//        return this.cartVariantRepository.findAll();
//    }
//
//    @Override
//    public CartVariant findById(Integer key) {
//        return this.cartVariantRepository.findById(key).orElse(null);
//    }
//
//    @Override
//    public List<CartVariant> findByName(String name) {
//        return null;
//    }
//
//    @Override
//    public CartVariant saveOrUpdate(CartVariant object) {
//        return this.cartVariantRepository.save(object);
//    }
//
//    @Override
//    public boolean existsById(Integer key) {
//        return this.cartVariantRepository.existsById(key);
//    }
//
//    @Override
//    public void delete(Integer key) {
//        this.cartVariantRepository.delete(findById(key));
//    }
//
//    @Override
//    public Page<CartVariant> pagination(Integer pageNo, Integer pageSize) {
//        return null;
//    }
//
//    @Override
//    public List<CartVariant> search(String keyWord) {
//        return null;
//    }
//
//    @Override
//    public Page<CartVariant> search(String keyWord, Integer pageNo, Integer pageSize) {
//        return null;
//    }
//}

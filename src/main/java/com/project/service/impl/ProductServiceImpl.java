package com.project.service.impl;

import com.project.dto.request.CategoryDTO;
import com.project.dto.request.ItemDTO;
import com.project.dto.request.ProductDTO;
import com.project.dto.request.SizeDTO;
import com.project.dto.response.ProductViewDTO;
import com.project.exception.base.CustomException;
import com.project.model.Category;
import com.project.model.Image;
import com.project.model.Item;
import com.project.model.Product;
import com.project.repository.ImageRepository;
import com.project.repository.ProductRepository;
import com.project.service.CategoryService;
import com.project.service.ItemService;
import com.project.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final CategoryService categoryService;
    private final ItemService itemService;
    private final ModelMapper modelMapper;

    @Override
    public List<ProductViewDTO> getAll() {
        return this.productRepository.findAll().stream()
                .map(item -> modelMapper.map(item, ProductViewDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductViewDTO> getAll(String keyword, Integer pageNo, Integer pageSize) {
        Page<Product> products;
        Pageable pageable;
        if (keyword == null) {
            pageable = PageRequest.of(pageNo-1, pageSize, Sort.by("createdAt").descending());
            products = this.productRepository.findAll(pageable);
        } else {
            List<Product> list = this.productRepository.listByName(keyword);
            if(list.isEmpty()){
                throw new CustomException.NotFoundException("Product not found with name : " + keyword, 404, new Date());
            } else{
                pageable = PageRequest.of(pageNo-1, pageSize);
                int start = (int) pageable.getOffset();
                int end = (pageable.getOffset() + pageable.getPageSize()) > list.size() ? list.size() : (int) (pageable.getOffset() + pageable.getPageSize());
                list = list.subList(start, end);
                products = new PageImpl<>(list, pageable, this.productRepository.listByName(keyword).size());
            }
        }
        if (!products.isEmpty()) {
            List<ProductViewDTO> productDtoList = products.getContent()
                    .stream()
                    .map(product -> modelMapper.map(product, ProductViewDTO.class))
                    .collect(Collectors.toList());

            return new PageImpl<>(productDtoList, pageable, products.getTotalElements());
        }
        return null;
    }

    @Override
    public Page<ProductViewDTO> getAll(String keyword, Integer categoryId, Float minPrice, Float maxPrice, Integer color, Integer size, Integer pageNo, Integer pageSize, String sortBy, String sortOrder) {
        Pageable pageable;
        Sort.Direction sortDirection = sortOrder.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        if (sortBy == null || sortBy.isEmpty()) {
            pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("createdAt").descending());
        } else {
            pageable = PageRequest.of(pageNo - 1, pageSize, sortDirection, sortBy);
        }

        Page<Product> products;
        if (keyword != null || categoryId != null || minPrice != null || maxPrice != null || color != null || size != null) {
            products = productRepository.filter(keyword, categoryId, minPrice, maxPrice, size, color, pageable);
        } else {
            pageable = PageRequest.of(pageNo-1, pageSize, Sort.by("createdAt").descending());
            products = this.productRepository.findAll(pageable);
        }

        if (products.isEmpty()) {
            return null;
        }
        List<ProductViewDTO> productDtoList = products.stream()
                .map(product -> modelMapper.map(product, ProductViewDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<>(productDtoList, pageable, products.getTotalElements());
    }

    @Override
    public ProductViewDTO findById(Integer id) {
        Product product = this.productRepository.findById(id).orElseThrow(()->new CustomException.NotFoundException("Product not found with id : " + id, 404, new Date()));
        return modelMapper.map(product, ProductViewDTO.class);
    }

    @Override
    public List<ProductViewDTO> findByName(String name) {
        if(name.isEmpty()){
            throw new CustomException.NotFoundException("Name product not empty", 404, new Date());
        }
        List<Product> list = productRepository.findByName(name);
        return list.stream().map(e -> modelMapper.map(e, ProductViewDTO.class)).collect(Collectors.toList());
    }

    @Override
    public ProductViewDTO save(ProductDTO productDto) {
        this.checkUnique(productDto.getName().trim());
        CategoryDTO category = this.categoryService.findById(productDto.getCategoryId());
        ItemDTO item = this.itemService.findById(productDto.getItemId());
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        Product productRequest = modelMapper.map(productDto, Product.class);
        productRequest.setCategory(modelMapper.map(category, Category.class));
        productRequest.setItem(modelMapper.map(item, Item.class));
        Product product = this.productRepository.save(productRequest);
        return modelMapper.map(product, ProductViewDTO.class);
    }

    @Override
    public ProductViewDTO update(ProductDTO productDto, Integer id) {
        ProductViewDTO p = this.findById(id);
        if(!Objects.equals(p.getName(), productDto.getName())){
            throw  new CustomException.NotImplementedException("Product name already taken", 501, new Date());
        }
        CategoryDTO category = this.categoryService.findById(productDto.getCategoryId());
        ItemDTO item = itemService.findById(productDto.getItemId());
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        Product product = modelMapper.map(p, Product.class);
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setDiscount(productDto.getDiscount());
        product.setImage(productDto.getImage());
        product.setCategory(modelMapper.map(category, Category.class));
        product.setItem(modelMapper.map(item, Item.class));
        product.setDescription(productDto.getDescription());
        product.setStatus(productDto.getStatus());
        product = this.productRepository.save(product);
        return modelMapper.map(product, ProductViewDTO.class);
    }

    @Override
    public void delete(Integer id, boolean check) {
        Product product = this.productRepository.findById(id).orElseThrow(()->new CustomException.NotFoundException("Product not found with id : " + id, 404, new Date()));
        List<Image> list = this.imageRepository.checkInProduct(id);
        if(!list.isEmpty()){
            if(!check){
                throw new CustomException.NotImplementedException("This product contains images", 501, new Date());
            } else {
                this.imageRepository.deleteAll(list);
            }
        } else {
            this.productRepository.delete(product);
        }

    }

    private void checkUnique(String name){
        List<Product> foundProduct = this.productRepository.findByName(name);
        if (!foundProduct.isEmpty()) {
            throw  new CustomException.NotImplementedException("Product name already taken", 501, new Date());
        }
    }
    @Override
    public Page<ProductViewDTO> filter(Integer categoryId, Integer pageNo, Integer pageSize){
        Page<Product> products;
        Pageable pageable;
        List<Product> list = this.productRepository.findByCategory(categoryId);
        if(list.isEmpty()){
            return  null;
        } else{
            pageable = PageRequest.of(pageNo-1, pageSize);
            int start = (int) pageable.getOffset();
            int end = (pageable.getOffset() + pageable.getPageSize()) > list.size() ? list.size() : (int) (pageable.getOffset() + pageable.getPageSize());
            list = list.subList(start, end);
            products = new PageImpl<>(list, pageable, this.productRepository.findByCategory(categoryId).size());
        }

        if (!products.isEmpty()) {
            List<ProductViewDTO> productDtoList = products.getContent()
                    .stream()
                    .map(product -> modelMapper.map(product, ProductViewDTO.class))
                    .collect(Collectors.toList());

            return new PageImpl<>(productDtoList, pageable, products.getTotalElements());
        }
        return null;
    }

}

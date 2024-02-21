package com.project.service.impl;

import com.project.dto.request.CategoryDTO;
import com.project.dto.request.ItemDTO;
import com.project.dto.request.ProductDTO;
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
    public Page<ProductViewDTO> getAll(String keyword, Integer pageNo, Integer pageSize) {
        Page<Product> products;
        Pageable pageable;
        if (keyword == null) {
            pageable = PageRequest.of(pageNo-1, pageSize, Sort.by("createdAt").descending());
            products = this.productRepository.findAll(pageable);
        } else {
            List<Product> list = this.productRepository.listByName(keyword);
            pageable = PageRequest.of(pageNo-1, pageSize);
            int start = (int) pageable.getOffset();
            int end = (pageable.getOffset() + pageable.getPageSize()) > list.size() ? list.size() : (int) (pageable.getOffset() + pageable.getPageSize());
            list = list.subList(start, end);
            products = new PageImpl<>(list, pageable, list.size());
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
    public ProductViewDTO findById(Integer id) {
        Product product = this.productRepository.findById(id).orElse(null);

        if(product == null) {
            throw new CustomException.NotFoundException("Product not found with id : " + id, 404, new Date());
        }
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
        this.checkUnique(productDto.getName().trim());
        CategoryDTO category = this.categoryService.findById(productDto.getCategoryId());
        ItemDTO item = itemService.findById(productDto.getItemId());
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        Product product = modelMapper.map(this.findById(id), Product.class);
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
        Product product = this.productRepository.findById(id).orElse(null);
        if(product == null) {
            throw new CustomException.NotFoundException("Product not found with id : " + id, 404, new Date());
        } else{
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

    }

    private void checkUnique(String name){
        List<Product> foundProduct = this.productRepository.findByName(name);
        if (!foundProduct.isEmpty()) {
            throw  new CustomException.NotImplementedException("Product name already taken", 501, new Date());
        }
    }
}

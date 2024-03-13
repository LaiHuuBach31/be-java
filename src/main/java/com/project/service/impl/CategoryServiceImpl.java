package com.project.service.impl;

import com.project.dto.request.CategoryDTO;
import com.project.exception.base.CustomException;
import com.project.model.Category;
import com.project.model.Product;
import com.project.repository.CategoryRepository;
import com.project.repository.ProductRepository;
import com.project.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CategoryDTO> getAll() {
        return this.categoryRepository.findAll().stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());
    }
    @Override
    public Page<CategoryDTO> getAll(String keyword, Integer pageNo, Integer pageSize) {
        Page<Category> categories;
        Pageable pageable;
        if (keyword == null) {
            pageable = PageRequest.of(pageNo-1, pageSize);
            categories = this.categoryRepository.findAll(pageable);
        } else {
            List<Category> list = this.categoryRepository.listByName(keyword);
            if(list.isEmpty()){
                throw new CustomException.NotFoundException("Category not found with name : " + keyword, 404, new Date());
            } else{
                pageable = PageRequest.of(pageNo-1, pageSize);
                int start = (int) pageable.getOffset();
                int end = (pageable.getOffset() + pageable.getPageSize()) > list.size() ? list.size() : (int) (pageable.getOffset() + pageable.getPageSize());
                list = list.subList(start, end);
                categories = new PageImpl<>(list, pageable, this.categoryRepository.listByName(keyword).size());
            }
        }
        if (!categories.isEmpty()) {
            List<CategoryDTO> categoryDtoList = categories.getContent()
                    .stream()
                    .map(category -> modelMapper.map(category, CategoryDTO.class))
                    .collect(Collectors.toList());

            return new PageImpl<>(categoryDtoList, pageable, categories.getTotalElements());
        }
        return null;
    }

    @Override
    public CategoryDTO findById(Integer id) {
        Category category = this.categoryRepository.findById(id).orElse(null);
        if(category == null) {
            throw new CustomException.NotFoundException("Category not found with id : " + id, 404, new Date());
        }
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public List<CategoryDTO> findByName(String name) {
        if(name.isEmpty()){
            throw new CustomException.NotFoundException("Name category not empty", 404, new Date());
        }
        List<Category> list = this.categoryRepository.findByName(name);
        return list.stream().map(e -> modelMapper.map(e, CategoryDTO.class)).collect(Collectors.toList());
    }

    @Override
    public CategoryDTO save(CategoryDTO categoryDto) {
        this.checkUnique(categoryDto.getName().trim());
        Category categoryRequest = modelMapper.map(categoryDto, Category.class);
        Category category = this.categoryRepository.save(categoryRequest);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO update(CategoryDTO categoryDto, Integer id) {
        this.checkUnique(categoryDto.getName().trim());
        Category category = modelMapper.map(this.findById(id), Category.class);
        category.setName(categoryDto.getName());
        category.setStatus(categoryDto.getStatus());
        category = this.categoryRepository.save(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public void delete(Integer id, boolean check) {
        Category category = this.categoryRepository.findById(id).orElse(null);
        if(category == null) {
            throw new CustomException.NotFoundException("Category not found with id : " + id, 404, new Date());
        } else{
            List<Product> list = this.productRepository.checkInCategory(id);
            if(!list.isEmpty()){
                if(!check){
                    throw new CustomException.NotImplementedException("This category contains products", 501, new Date());
                } else {
                    this.productRepository.deleteAll(list);
                }
            } else {
                this.categoryRepository.delete(category);
            }
        }
    }

    private void checkUnique(String name){
        List<Category> foundCategory = this.categoryRepository.findByName(name);
        if (!foundCategory.isEmpty()) {
            throw  new CustomException.NotImplementedException("Category name already taken", 501, new Date());
        }
    }
}

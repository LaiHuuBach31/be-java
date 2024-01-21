package com.project.service.impl;

import com.project.model.Category;
import com.project.repository.CategoryRepository;
import com.project.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAll() {
        return this.categoryRepository.findAll();
    }

    @Override
    public Category findById(Integer id) {
        return this.categoryRepository.findById(id).orElse(null);
        // if else xong throw new NoCategoryFoundException("Không tìm thấy category")
    }

    @Override
    public List<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Category saveOrUpdate(Category category) {
        return this.categoryRepository.save(category);
    }

    @Override
    public boolean existsById(Integer id) {
        return this.categoryRepository.existsById(id);
    }

    @Override
    public void delete(Integer id) {
        this.categoryRepository.delete(this.findById(id));
    }

    @Override
    public Page<Category> pagination(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        return this.categoryRepository.findAll(pageable);
    }

    @Override
    public List<Category> search(String keyWord) {
        return this.categoryRepository.listByName(keyWord);
    }

    @Override
    public Page<Category> search(String keyWord, Integer pageNo, Integer pageSize) {
        List<Category> list = this.search(keyWord);
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        int start = (int) pageable.getOffset();
        int end = (pageable.getOffset() + pageable.getPageSize()) > list.size() ? list.size() : (int) (pageable.getOffset() + pageable.getPageSize());
        list = list.subList(start, end);
        return new PageImpl<Category>(list, pageable, list.size());
    }



}

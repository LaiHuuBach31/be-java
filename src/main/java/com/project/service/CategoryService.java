package com.project.service;


import com.project.model.Category;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {
    List<Category> getAll();
    Category findById(Integer id);
    List<Category> findByName(String name);
    Category saveOrUpdate(Category category);
    boolean existsById(Integer id);
    void delete(Integer id);
    Page<Category> pagination(Integer pageNo, Integer pageSize);
    List<Category> search(String keyWord);
    Page<Category> search(String keyWord ,Integer pageNo, Integer pageSize);


}

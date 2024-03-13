package com.project.service;


import com.project.dto.request.CategoryDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAll();
    Page<CategoryDTO> getAll(String keyword, Integer pageNo, Integer pageSize);
    CategoryDTO findById(Integer id);
    List<CategoryDTO> findByName(String name);
    CategoryDTO save(CategoryDTO categoryDto);
    CategoryDTO update(CategoryDTO categoryDto, Integer id);
    void delete(Integer id, boolean check);

}

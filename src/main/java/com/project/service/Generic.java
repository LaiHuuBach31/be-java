package com.project.service;

import org.springframework.data.domain.Page;

import java.util.List;

public interface Generic<I, O> {
    Page<O> getAll(String keyword, Integer pageNo, Integer pageSize);
    O findById(Integer id);
    List<O> findByName(String name);
    O save(I object);
    O update(I object, Integer id);
    void delete(Integer id, boolean check);
}

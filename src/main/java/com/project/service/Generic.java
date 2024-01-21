package com.project.service;

import org.springframework.data.domain.Page;

import java.util.List;

public interface Generic<T, K> {
    List<T> getAll();
    T findById(K key);
    List<T> findByName(String name);
    T saveOrUpdate(T object);
    boolean existsById(K key);
    void delete(K key);
    Page<T> pagination(Integer pageNo, Integer pageSize);
    List<T> search(String keyWord);
    Page<T> search(String keyWord ,Integer pageNo, Integer pageSize);
}

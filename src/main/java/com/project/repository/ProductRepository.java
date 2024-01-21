package com.project.repository;


import com.project.model.Category;
import com.project.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByName(String name);
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> listByName(String name);

    @Modifying
    @Query( "DELETE  FROM Product p WHERE p.category.id = :categoryId")
    void deleteByCategory(Integer categoryId);
}

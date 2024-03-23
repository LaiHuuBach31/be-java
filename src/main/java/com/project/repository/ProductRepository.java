package com.project.repository;


import com.project.model.Category;
import com.project.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByName(String name);
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> listByName(String name);

    @Query( "SELECT p FROM Product p WHERE p.category.id = :categoryId")
    List<Product> checkInCategory(Integer categoryId);

    @Query( "SELECT p FROM Product p WHERE p.item.id = :itemId")
    List<Product> checkInItem(Integer itemId);
    @Query( "SELECT p FROM Product p WHERE p.category.id = :categoryId")
    List<Product> findByCategory(Integer categoryId);

    @Query("SELECT p FROM Product p JOIN VariantProduct vp ON p.id = vp.product.id JOIN Color c ON c.id = vp.color.id JOIN Size s ON s.id = vp.size.id " +
            "WHERE (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:categoryId IS NULL OR p.category.id = :categoryId) " +
            "AND (:minPrice IS NULL OR :maxPrice IS NULL OR p.price BETWEEN :minPrice AND :maxPrice) " +
            "AND (:colorId IS NULL OR vp.color.id = :colorId) " +
            "AND (:sizeId IS NULL OR vp.size.id = :sizeId)")
    Page<Product> filter(String keyword, Integer categoryId, Float minPrice, Float maxPrice, Integer sizeId, Integer colorId, Pageable pageable);
}

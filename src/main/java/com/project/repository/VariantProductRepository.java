package com.project.repository;

import com.project.model.VariantProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariantProductRepository extends JpaRepository<VariantProduct, Integer> {
    @Query("SELECT vp FROM VariantProduct vp WHERE vp.product.id = :productId AND vp.color.id = :colorId AND vp.size.id = :sizeId")
    VariantProduct checkAttributes(Integer productId, Integer colorId, Integer sizeId);

}

package com.project.repository;

import com.project.model.Color;
import com.project.model.Product;
import com.project.model.VariantProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariantProductRepository extends JpaRepository<VariantProduct, Integer> {
    @Query("SELECT vp FROM VariantProduct vp WHERE vp.product.id = :productId AND vp.color.id = :colorId AND vp.size.id = :sizeId")
    VariantProduct checkAttributes(Integer productId, Integer colorId, Integer sizeId);

    @Query( "SELECT vp FROM VariantProduct vp WHERE vp.color.id = : colorId")
    List<VariantProduct> checkInColor(Integer colorId);

    @Query( "SELECT vp FROM VariantProduct vp WHERE vp.size.id = : sizeId")
    List<VariantProduct> checkInSize(Integer sizeId);
}

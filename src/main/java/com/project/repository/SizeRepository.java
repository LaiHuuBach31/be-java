package com.project.repository;

import com.project.model.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SizeRepository extends JpaRepository<Size, Integer> {
    List<Size> findByName(String name);
    @Query("SELECT s FROM Size s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Size> listByName(String name);
}

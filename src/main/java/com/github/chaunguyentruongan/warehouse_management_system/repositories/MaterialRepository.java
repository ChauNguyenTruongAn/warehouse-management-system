package com.github.chaunguyentruongan.warehouse_management_system.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.github.chaunguyentruongan.warehouse_management_system.models.Material;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    Optional<Material> findByCode(String code);

       @Query("""
        SELECT m FROM Material m
        WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(m.code) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    Page<Material> search(String keyword, Pageable pageable);
}

package com.github.chaunguyentruongan.warehouse_management_system.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.chaunguyentruongan.warehouse_management_system.models.Material;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    Optional<Material> findByCode(String code);
}

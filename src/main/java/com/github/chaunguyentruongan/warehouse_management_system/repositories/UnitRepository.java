package com.github.chaunguyentruongan.warehouse_management_system.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.chaunguyentruongan.warehouse_management_system.models.Unit;

public interface UnitRepository extends JpaRepository<Unit, Long> {
    Optional<Unit> findByName(String name);
}

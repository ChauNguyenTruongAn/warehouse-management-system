package com.github.chaunguyentruongan.warehouse_management_system.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.github.chaunguyentruongan.warehouse_management_system.models.Unit;

public interface UnitRepository extends JpaRepository<Unit, Long> {
    Optional<Unit> findByName(String name);

    @Query("""
                SELECT u FROM Unit u
                WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<Unit> search(String keyword, Pageable pageable);
}

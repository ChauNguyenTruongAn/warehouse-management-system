package com.github.chaunguyentruongan.warehouse_management_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.chaunguyentruongan.warehouse_management_system.models.ExportItem;

@Repository
public interface ExportItemRepository extends JpaRepository<ExportItem, Long> {
}
package com.github.chaunguyentruongan.warehouse_management_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.chaunguyentruongan.warehouse_management_system.models.ExportReceipt;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExportReceiptRepository extends JpaRepository<ExportReceipt, Long> {

    @Query("""
                SELECT r FROM ExportReceipt r
                WHERE (:keyword IS NULL OR LOWER(r.code) LIKE LOWER(CONCAT('%', :keyword, '%')))
                AND (:fromDate IS NULL OR r.exportDate >= :fromDate)
                AND (:toDate IS NULL OR r.exportDate <= :toDate)
            """)
    Page<ExportReceipt> search(String keyword, LocalDate fromDate, LocalDate toDate, Pageable pageable);

    @Query("SELECT SUM(e.quantity) FROM ExportItem e WHERE e.material.id = :materialId")
    Integer sumQuantityByMaterialId(@Param("materialId") Long materialId);

    List<ExportReceipt> findAllByExportDateBetween(LocalDate from, LocalDate to);
}
package com.github.chaunguyentruongan.warehouse_management_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.chaunguyentruongan.warehouse_management_system.models.ImportItem;
import com.github.chaunguyentruongan.warehouse_management_system.models.ImportReceipt;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImportReceiptRepository extends JpaRepository<ImportReceipt, Long> {

    @Query("""
                SELECT r FROM ImportReceipt r
                WHERE (:keyword IS NULL OR LOWER(r.code) LIKE LOWER(CONCAT('%', :keyword, '%')))
                AND (:fromDate IS NULL OR r.importDate >= :fromDate)
                AND (:toDate IS NULL OR r.importDate <= :toDate)
            """)
    Page<ImportReceipt> search(String keyword, LocalDate fromDate, LocalDate toDate, Pageable pageable);

    @Query("SELECT SUM(i.quantity) FROM ImportItem i WHERE i.material.id = :materialId")
    Integer sumQuantityByMaterialId(@Param("materialId") Long materialId);

    List<ImportReceipt> findAllByImportDateBetween(LocalDate from, LocalDate to);

    @Query("SELECT i FROM ImportItem i WHERE i.receipt.id = :receiptId")
    List<ImportItem> findItemsByReceiptId(@Param("receiptId") Long receiptId);
}
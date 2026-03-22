package com.github.chaunguyentruongan.warehouse_management_system.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.github.chaunguyentruongan.warehouse_management_system.models.ExportItem;

@Repository
public interface ExportItemRepository extends JpaRepository<ExportItem, Long> {
    @Query("SELECT e FROM ExportItem e WHERE e.receipt.id = :receiptId")
    List<ExportItem> findItemsByReceiptId(@Param("receiptId") Long receiptId);
}
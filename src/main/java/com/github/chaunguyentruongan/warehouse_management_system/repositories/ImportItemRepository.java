package com.github.chaunguyentruongan.warehouse_management_system.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.github.chaunguyentruongan.warehouse_management_system.models.ImportItem;

@Repository
public interface ImportItemRepository extends JpaRepository<ImportItem, Long> {

        // ImportReceiptRepository
    @Query("SELECT i FROM ImportItem i WHERE i.receipt.id = :receiptId")
    List<ImportItem> findItemsByReceiptId(@Param("receiptId") Long receiptId);

}
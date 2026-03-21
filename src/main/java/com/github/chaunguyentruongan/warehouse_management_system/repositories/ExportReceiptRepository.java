package com.github.chaunguyentruongan.warehouse_management_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.chaunguyentruongan.warehouse_management_system.models.ExportReceipt;

public interface ExportReceiptRepository extends JpaRepository<ExportReceipt, Long> {
}

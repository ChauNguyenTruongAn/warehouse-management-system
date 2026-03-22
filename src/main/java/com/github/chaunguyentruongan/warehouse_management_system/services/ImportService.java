package com.github.chaunguyentruongan.warehouse_management_system.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.chaunguyentruongan.warehouse_management_system.dto.ImportItemRequest;
import com.github.chaunguyentruongan.warehouse_management_system.dto.ImportRequest;
import com.github.chaunguyentruongan.warehouse_management_system.models.ImportItem;
import com.github.chaunguyentruongan.warehouse_management_system.models.ImportReceipt;
import com.github.chaunguyentruongan.warehouse_management_system.models.Material;
import com.github.chaunguyentruongan.warehouse_management_system.repositories.ImportItemRepository;
import com.github.chaunguyentruongan.warehouse_management_system.repositories.ImportReceiptRepository;
import com.github.chaunguyentruongan.warehouse_management_system.repositories.MaterialRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImportService {

    private final ImportReceiptRepository receiptRepo;
    private final MaterialRepository materialRepo;
    private final StockService stockService;

    // ================= CREATE =================
    @Transactional
    public ImportReceipt create(ImportRequest request) {

        ImportReceipt receipt = new ImportReceipt();
        receipt.setCode(generateCode());
        receipt.setImportDate(request.getImportDate());
        receipt.setNote(request.getNote());

        List<ImportItem> items = new ArrayList<>();

        for (ImportItemRequest i : request.getItems()) {

            Material material = materialRepo.findById(i.getMaterialId())
                    .orElseThrow(() -> new RuntimeException("Material not found"));

            if (i.getQuantity() <= 0) {
                throw new RuntimeException("Quantity must > 0");
            }

            ImportItem item = new ImportItem();
            item.setMaterial(material);
            item.setQuantity(i.getQuantity());
            item.setReceipt(receipt);

            items.add(item);

            stockService.updateStock(material.getId(), i.getQuantity());
        }

        receipt.setItems(items);

        return receiptRepo.save(receipt);
    }

    // ================= UPDATE =================
    @Transactional
    public ImportReceipt update(Long id, ImportRequest request) {

        ImportReceipt old = receiptRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        // rollback stock cũ
        for (ImportItem item : old.getItems()) {
            stockService.updateStock(item.getMaterial().getId(), -item.getQuantity());
        }

        old.getItems().clear();

        List<ImportItem> newItems = new ArrayList<>();

        for (ImportItemRequest i : request.getItems()) {

            Material material = materialRepo.findById(i.getMaterialId())
                    .orElseThrow(() -> new RuntimeException("Material not found"));

            ImportItem item = new ImportItem();
            item.setMaterial(material);
            item.setQuantity(i.getQuantity());
            item.setReceipt(old);

            newItems.add(item);

            stockService.updateStock(material.getId(), i.getQuantity());
        }

        old.setItems(newItems);
        old.setImportDate(request.getImportDate());
        old.setNote(request.getNote());

        return receiptRepo.save(old);
    }

    // ================= DELETE =================
    @Transactional
    public void delete(Long id) {

        ImportReceipt receipt = receiptRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        // rollback stock
        for (ImportItem item : receipt.getItems()) {
            stockService.updateStock(item.getMaterial().getId(), -item.getQuantity());
        }

        receiptRepo.delete(receipt);
    }

    // ================= LIST =================
    public Page<ImportReceipt> getAll(Pageable pageable) {
        return receiptRepo.findAll(pageable);
    }

    public Page<ImportReceipt> search(String keyword, LocalDate from, LocalDate to, Pageable pageable) {
        return receiptRepo.search(keyword, from, to, pageable);
    }

    public ImportReceipt getById(Long id) {
        return receiptRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
    }

    // ================= CODE GENERATE =================
    private String generateCode() {
        return "IMP-" + LocalDate.now() + "-" + System.currentTimeMillis();
    }
}
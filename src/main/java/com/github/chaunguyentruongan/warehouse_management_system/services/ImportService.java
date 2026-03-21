package com.github.chaunguyentruongan.warehouse_management_system.services;

import java.util.ArrayList;
import java.util.List;

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

    private final ImportReceiptRepository importReceiptRepository;
    private final ImportItemRepository importItemRepository;
    private final MaterialRepository materialRepository;
    private final StockService stockService;

    @Transactional
    public void createImport(ImportRequest request) {

        ImportReceipt receipt = new ImportReceipt();
        receipt.setImportDate(request.getImportDate());
        receipt.setNote(request.getNote());

        List<ImportItem> items = new ArrayList<>();

        for (ImportItemRequest itemReq : request.getItems()) {
            Material material = materialRepository.findById(itemReq.getMaterialId())
                    .orElseThrow(() -> new RuntimeException("Material not found: " + itemReq.getMaterialId()));

            if (itemReq.getQuantity() <= 0) {
                throw new RuntimeException("Quantity must be > 0");
            }

            ImportItem item = new ImportItem();
            item.setMaterial(material);
            item.setQuantity(itemReq.getQuantity());
            item.setReceipt(receipt);

            items.add(item);

            // Cập nhật stock
            stockService.updateStock(material.getId(), itemReq.getQuantity());
        }

        receipt.setItems(items);
        importReceiptRepository.save(receipt);
        importItemRepository.saveAll(items);
    }
}
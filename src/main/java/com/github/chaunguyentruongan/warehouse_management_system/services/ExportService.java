package com.github.chaunguyentruongan.warehouse_management_system.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.chaunguyentruongan.warehouse_management_system.dto.ExportItemRequest;
import com.github.chaunguyentruongan.warehouse_management_system.dto.ExportRequest;
import com.github.chaunguyentruongan.warehouse_management_system.models.ExportItem;
import com.github.chaunguyentruongan.warehouse_management_system.models.ExportReceipt;
import com.github.chaunguyentruongan.warehouse_management_system.models.Material;
import com.github.chaunguyentruongan.warehouse_management_system.repositories.ExportItemRepository;
import com.github.chaunguyentruongan.warehouse_management_system.repositories.ExportReceiptRepository;
import com.github.chaunguyentruongan.warehouse_management_system.repositories.MaterialRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExportService {

    private final ExportReceiptRepository exportReceiptRepository;
    private final ExportItemRepository exportItemRepository;
    private final MaterialRepository materialRepository;
    private final StockService stockService;

    @Transactional
    public void createExport(ExportRequest request) {

        ExportReceipt receipt = new ExportReceipt();
        receipt.setExportDate(request.getExportDate());
        receipt.setRecipient(request.getRecipient());
        receipt.setDepartment(request.getDepartment());
        receipt.setPurpose(request.getPurpose());
        receipt.setNote(request.getNote());

        List<ExportItem> items = new ArrayList<>();

        for (ExportItemRequest itemReq : request.getItems()) {
            Material material = materialRepository.findById(itemReq.getMaterialId())
                    .orElseThrow(() -> new RuntimeException("Material not found: " + itemReq.getMaterialId()));

            if (itemReq.getQuantity() <= 0) {
                throw new RuntimeException("Quantity must be > 0");
            }

            // Kiểm tra tồn kho trước khi export
            int stock = stockService.getStock(material.getId());
            if (stock < itemReq.getQuantity()) {
                throw new RuntimeException("Not enough stock for material: " + material.getName());
            }

            ExportItem item = new ExportItem();
            item.setMaterial(material);
            item.setQuantity(itemReq.getQuantity());
            item.setReceipt(receipt);

            items.add(item);

            // Cập nhật stock
            stockService.updateStock(material.getId(), -itemReq.getQuantity());
        }

        receipt.setItems(items);
        exportReceiptRepository.save(receipt);
        exportItemRepository.saveAll(items);
    }
}
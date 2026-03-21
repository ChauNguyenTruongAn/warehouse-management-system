package com.github.chaunguyentruongan.warehouse_management_system.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.chaunguyentruongan.warehouse_management_system.dto.ExportItemRequest;
import com.github.chaunguyentruongan.warehouse_management_system.dto.ExportRequest;
import com.github.chaunguyentruongan.warehouse_management_system.models.ExportItem;
import com.github.chaunguyentruongan.warehouse_management_system.models.ExportReceipt;
import com.github.chaunguyentruongan.warehouse_management_system.models.Material;
import com.github.chaunguyentruongan.warehouse_management_system.repositories.ExportReceiptRepository;
import com.github.chaunguyentruongan.warehouse_management_system.repositories.MaterialRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExportService {

    private final MaterialRepository materialRepo;
    private final ExportReceiptRepository exportRepo;

    @Transactional
    public void create(ExportRequest request) {

        ExportReceipt receipt = new ExportReceipt();
        receipt.setExportDate(request.getExportDate());
        receipt.setRecipient(request.getRecipient());
        receipt.setDepartment(request.getDepartment());

        List<ExportItem> items = new ArrayList<>();

        for (ExportItemRequest itemReq : request.getItems()) {

            Material material = materialRepo.findById(itemReq.getMaterialId())
                    .orElseThrow(() -> new RuntimeException("Material not found"));

            int stock = getStock(material.getId());

            if (stock < itemReq.getQuantity()) {
                throw new RuntimeException("Not enough stock");
            }

            ExportItem item = new ExportItem();
            item.setMaterial(material);
            item.setQuantity(itemReq.getQuantity());
            item.setReceipt(receipt);

            items.add(item);
        }

        receipt.setItems(items);

        exportRepo.save(receipt);
    }

    private int getStock(Long materialId) {
        // TODO: query sum import - export
        return 100;
    }
}
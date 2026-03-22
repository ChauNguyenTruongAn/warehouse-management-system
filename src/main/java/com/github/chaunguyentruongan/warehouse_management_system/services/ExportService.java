package com.github.chaunguyentruongan.warehouse_management_system.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    private final ExportReceiptRepository receiptRepo;
    private final MaterialRepository materialRepo;
    private final StockService stockService;

    // ================= CREATE =================
    @Transactional
    public ExportReceipt create(ExportRequest request) {

        ExportReceipt receipt = new ExportReceipt();
        receipt.setCode(generateCode());
        receipt.setExportDate(request.getExportDate());
        receipt.setRecipient(request.getRecipient());
        receipt.setDepartment(request.getDepartment());
        receipt.setPurpose(request.getPurpose());
        receipt.setNote(request.getNote());

        List<ExportItem> items = new ArrayList<>();

        for (ExportItemRequest i : request.getItems()) {

            Material material = materialRepo.findById(i.getMaterialId())
                    .orElseThrow(() -> new RuntimeException("Material not found"));

            if (i.getQuantity() <= 0) {
                throw new RuntimeException("Số lượng phải > 0");
            }

            int stock = stockService.getStock(material.getId());
            if (stock < i.getQuantity()) {
                throw new RuntimeException("Không đủ tồn kho: " + material.getName());
            }

            ExportItem item = new ExportItem();
            item.setMaterial(material);
            item.setQuantity(i.getQuantity());
            item.setReceipt(receipt);

            items.add(item);

            stockService.updateStock(material.getId(), -i.getQuantity());
        }

        receipt.setItems(items);

        return receiptRepo.save(receipt);
    }

    // ================= UPDATE =================
    @Transactional
    public ExportReceipt update(Long id, ExportRequest request) {

        ExportReceipt old = receiptRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu"));

        // rollback stock cũ
        for (ExportItem item : old.getItems()) {
            stockService.updateStock(item.getMaterial().getId(), item.getQuantity());
        }

        old.getItems().clear();

        List<ExportItem> newItems = new ArrayList<>();

        for (ExportItemRequest i : request.getItems()) {

            Material material = materialRepo.findById(i.getMaterialId())
                    .orElseThrow(() -> new RuntimeException("Material not found"));

            int stock = stockService.getStock(material.getId());
            if (stock < i.getQuantity()) {
                throw new RuntimeException("Không đủ tồn kho: " + material.getName());
            }

            ExportItem item = new ExportItem();
            item.setMaterial(material);
            item.setQuantity(i.getQuantity());
            item.setReceipt(old);

            newItems.add(item);

            stockService.updateStock(material.getId(), -i.getQuantity());
        }

        old.setItems(newItems);
        old.setExportDate(request.getExportDate());
        old.setRecipient(request.getRecipient());
        old.setDepartment(request.getDepartment());
        old.setPurpose(request.getPurpose());
        old.setNote(request.getNote());

        return receiptRepo.save(old);
    }

    // ================= DELETE =================
    @Transactional
    public void delete(Long id) {

        ExportReceipt receipt = receiptRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy"));

        // rollback stock
        for (ExportItem item : receipt.getItems()) {
            stockService.updateStock(item.getMaterial().getId(), item.getQuantity());
        }

        receiptRepo.delete(receipt);
    }

    // ================= LIST =================
    public Page<ExportReceipt> getAll(Pageable pageable) {
        return receiptRepo.findAll(pageable);
    }

    public Page<ExportReceipt> search(String keyword, LocalDate from, LocalDate to, Pageable pageable) {
        return receiptRepo.search(keyword, from, to, pageable);
    }

    public ExportReceipt getById(Long id) {
        return receiptRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy"));
    }

    // ================= CODE =================
    private String generateCode() {
        return "EXP-" + LocalDate.now() + "-" + System.currentTimeMillis();
    }
}
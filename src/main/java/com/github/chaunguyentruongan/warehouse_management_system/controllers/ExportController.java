package com.github.chaunguyentruongan.warehouse_management_system.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.chaunguyentruongan.warehouse_management_system.dto.ExportRequest;
import com.github.chaunguyentruongan.warehouse_management_system.dto.ReceiptDetailDTO;
import com.github.chaunguyentruongan.warehouse_management_system.models.ExportItem;
import com.github.chaunguyentruongan.warehouse_management_system.repositories.ExportItemRepository;
import com.github.chaunguyentruongan.warehouse_management_system.services.ExportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/exports")
@RequiredArgsConstructor
public class ExportController {

    private final ExportService exportService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ExportRequest request) {
        return ResponseEntity.ok(exportService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
            @RequestBody ExportRequest request) {
        return ResponseEntity.ok(exportService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        exportService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            Pageable pageable) {

        if (keyword != null || fromDate != null || toDate != null) {
            return ResponseEntity.ok(
                    exportService.search(keyword, fromDate, toDate, pageable));
        }

        return ResponseEntity.ok(exportService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        return ResponseEntity.ok(exportService.getById(id));
    }
}

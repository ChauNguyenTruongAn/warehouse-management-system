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

import com.github.chaunguyentruongan.warehouse_management_system.dto.ImportRequest;
import com.github.chaunguyentruongan.warehouse_management_system.dto.ReceiptDetailDTO;
import com.github.chaunguyentruongan.warehouse_management_system.models.ExportItem;
import com.github.chaunguyentruongan.warehouse_management_system.models.ImportItem;
import com.github.chaunguyentruongan.warehouse_management_system.repositories.ImportReceiptRepository;
import com.github.chaunguyentruongan.warehouse_management_system.services.ImportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/imports")
@RequiredArgsConstructor
public class ImportController {

    private final ImportService importService;
    private final ImportReceiptRepository importItemRepository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ImportRequest request) {
        return ResponseEntity.ok(importService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
            @RequestBody ImportRequest request) {
        return ResponseEntity.ok(importService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        importService.delete(id);
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
                    importService.search(keyword, fromDate, toDate, pageable));
        }

        return ResponseEntity.ok(importService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        return ResponseEntity.ok(importService.getById(id));
    }

    @GetMapping("/{id}/detail")
public ResponseEntity<List<ReceiptDetailDTO>> importDetail(@PathVariable Long id){
    List<ImportItem> items = importItemRepository.findItemsByReceiptId(id);
    List<ReceiptDetailDTO> details = items.stream().map(i -> 
        new ReceiptDetailDTO(i.getMaterial().getName(), i.getMaterial().getUnit().getName(), i.getQuantity())
    ).toList();
    return ResponseEntity.ok(details);
}


}
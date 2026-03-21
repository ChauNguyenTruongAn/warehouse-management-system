package com.github.chaunguyentruongan.warehouse_management_system.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.chaunguyentruongan.warehouse_management_system.dto.ExportRequest;
import com.github.chaunguyentruongan.warehouse_management_system.dto.ImportRequest;
import com.github.chaunguyentruongan.warehouse_management_system.services.ExportService;
import com.github.chaunguyentruongan.warehouse_management_system.services.ImportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WarehouseController {

    private final ImportService importService;
    private final ExportService exportService;
    
     @PostMapping("/imports")
    public ResponseEntity<?> createImport(@RequestBody ImportRequest request) {
        importService.createImport(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/exports")
    public ResponseEntity<?> createExport(@RequestBody ExportRequest request) {
        exportService.createExport(request);
        return ResponseEntity.ok().build();
    }
}
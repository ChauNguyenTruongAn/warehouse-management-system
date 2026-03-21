package com.github.chaunguyentruongan.warehouse_management_system.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.chaunguyentruongan.warehouse_management_system.dto.ExportRequest;
import com.github.chaunguyentruongan.warehouse_management_system.services.ExportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/exports")
@RequiredArgsConstructor
public class ExportController {

    private final ExportService service;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ExportRequest request) {
        service.create(request);
        return ResponseEntity.ok().build();
    }
}
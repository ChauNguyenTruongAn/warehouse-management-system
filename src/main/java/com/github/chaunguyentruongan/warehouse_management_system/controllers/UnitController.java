package com.github.chaunguyentruongan.warehouse_management_system.controllers;

import org.springframework.data.domain.Page;
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

import com.github.chaunguyentruongan.warehouse_management_system.models.Unit;
import com.github.chaunguyentruongan.warehouse_management_system.services.UnitService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/units")
@RequiredArgsConstructor
public class UnitController {

    private final UnitService unitService;

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<Unit> create(@RequestBody Unit request) {
        return ResponseEntity.ok(unitService.create(request));
    }

    // ================= GET LIST (PAGE + SEARCH) =================
    @GetMapping
    public ResponseEntity<Page<Unit>> getAll(
            @RequestParam(required = false) String keyword,
            Pageable pageable) {

        if (keyword != null && !keyword.isBlank()) {
            return ResponseEntity.ok(unitService.search(keyword, pageable));
        }

        return ResponseEntity.ok(unitService.getAll(pageable));
    }

    // ================= DETAIL =================
    @GetMapping("/{id}")
    public ResponseEntity<Unit> getById(@PathVariable Long id) {
        return ResponseEntity.ok(unitService.getById(id));
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<Unit> update(
            @PathVariable Long id,
            @RequestBody Unit request) {

        return ResponseEntity.ok(unitService.update(id, request));
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        unitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
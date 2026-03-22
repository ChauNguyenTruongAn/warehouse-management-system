package com.github.chaunguyentruongan.warehouse_management_system.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.github.chaunguyentruongan.warehouse_management_system.dto.MaterialRequest;
import com.github.chaunguyentruongan.warehouse_management_system.models.Material;
import com.github.chaunguyentruongan.warehouse_management_system.services.MaterialService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;

    // ===================== CREATE =====================
    @PostMapping
    public ResponseEntity<Material> createMaterial(@RequestBody MaterialRequest request) {
        return ResponseEntity.ok(materialService.addMaterial(request));
    }

    // ===================== GET LIST (PAGINATION + SEARCH) =====================
    @GetMapping
    public ResponseEntity<Page<Material>> getAllMaterials(
            @RequestParam(required = false) String keyword,
            Pageable pageable) {

        if (keyword != null && !keyword.isBlank()) {
            return ResponseEntity.ok(materialService.searchMaterials(keyword, pageable));
        }

        return ResponseEntity.ok(materialService.getAllMaterials(pageable));
    }

    // ===================== GET DETAIL =====================
    @GetMapping("/{id}")
    public ResponseEntity<Material> getMaterialById(@PathVariable Long id) {
        return ResponseEntity.ok(materialService.getMaterialById(id));
    }

    // ===================== UPDATE =====================
    @PutMapping("/{id}")
    public ResponseEntity<Material> updateMaterial(
            @PathVariable Long id,
            @RequestBody MaterialRequest request) {

        return ResponseEntity.ok(materialService.updateMaterial(id, request));
    }

    // ===================== DELETE =====================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long id) {
        materialService.deleteMaterial(id);
        return ResponseEntity.noContent().build();
    }
}
package com.github.chaunguyentruongan.warehouse_management_system.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.chaunguyentruongan.warehouse_management_system.dto.MaterialRequest;
import com.github.chaunguyentruongan.warehouse_management_system.models.Material;
import com.github.chaunguyentruongan.warehouse_management_system.models.Unit;
import com.github.chaunguyentruongan.warehouse_management_system.repositories.MaterialRepository;
import com.github.chaunguyentruongan.warehouse_management_system.repositories.UnitRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialRepository materialRepository;
    private final UnitRepository unitRepository;

    @PostMapping
    public ResponseEntity<?> createMaterial(@RequestBody MaterialRequest request) {
        Unit unit = unitRepository.findById(request.getUnitId())
                .orElseThrow(() -> new RuntimeException("Unit not found"));
        Material material = new Material();
        material.setCode(request.getCode());
        material.setName(request.getName());
        material.setUnit(unit);
        material.setMinStock(request.getMinStock());
        material.setDescription(request.getDescription());

        Material saved = materialRepository.save(material);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Material>> getAllMaterials() {
        return ResponseEntity.ok(materialRepository.findAll());
    }
}
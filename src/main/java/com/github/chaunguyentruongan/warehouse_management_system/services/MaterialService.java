package com.github.chaunguyentruongan.warehouse_management_system.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.chaunguyentruongan.warehouse_management_system.dto.MaterialRequest;
import com.github.chaunguyentruongan.warehouse_management_system.models.Material;
import com.github.chaunguyentruongan.warehouse_management_system.models.Unit;
import com.github.chaunguyentruongan.warehouse_management_system.repositories.MaterialRepository;
import com.github.chaunguyentruongan.warehouse_management_system.repositories.UnitRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final UnitRepository unitRepository;

    // ===================== CREATE =====================
    public Material addMaterial(MaterialRequest request) {

        // check duplicate code
        if (materialRepository.findByCode(request.getCode()).isPresent()) {
            throw new RuntimeException("Material code already exists: " + request.getCode());
        }

        Unit unit = unitRepository.findById(request.getUnitId())
                .orElseThrow(() -> new RuntimeException("Not found unit with id: " + request.getUnitId()));

        Material material = new Material();
        material.setCode(request.getCode());
        material.setName(request.getName());
        material.setDescription(request.getDescription());
        material.setMinStock(request.getMinStock());
        material.setUnit(unit);

        return materialRepository.save(material);
    }

    // ===================== READ (LIST + PAGINATION) =====================
    public Page<Material> getAllMaterials(Pageable pageable) {
        return materialRepository.findAll(pageable);
    }

    // ===================== READ (DETAIL) =====================
    public Material getMaterialById(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material not found with id: " + id));
    }

    public Material getMaterialByCode(String code) {
        return materialRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Material not found with code: " + code));
    }

    // ===================== UPDATE =====================
    public Material updateMaterial(Long id, MaterialRequest request) {

        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material not found with id: " + id));

        // check duplicate code (nếu đổi code)
        Optional<Material> existing = materialRepository.findByCode(request.getCode());
        if (existing.isPresent() && !existing.get().getId().equals(id)) {
            throw new RuntimeException("Material code already exists: " + request.getCode());
        }

        Unit unit = unitRepository.findById(request.getUnitId())
                .orElseThrow(() -> new RuntimeException("Not found unit with id: " + request.getUnitId()));

        material.setCode(request.getCode());
        material.setName(request.getName());
        material.setDescription(request.getDescription());
        material.setMinStock(request.getMinStock());
        material.setUnit(unit);

        return materialRepository.save(material);
    }

    // ===================== DELETE =====================
    public void deleteMaterial(Long id) {

        if (!materialRepository.existsById(id)) {
            throw new RuntimeException("Material not found with id: " + id);
        }

        materialRepository.deleteById(id);
    }

    // ===================== SEARCH (ADMIN FEATURE) =====================
    public Page<Material> searchMaterials(String keyword, Pageable pageable) {
        // cần custom query trong repository
        return materialRepository.search(keyword, pageable);
    }
}
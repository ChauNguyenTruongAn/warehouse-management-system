package com.github.chaunguyentruongan.warehouse_management_system.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.chaunguyentruongan.warehouse_management_system.models.Unit;
import com.github.chaunguyentruongan.warehouse_management_system.repositories.UnitRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UnitService {

    private final UnitRepository unitRepository;

    // ================= CREATE =================
    public Unit create(Unit request) {

        if (unitRepository.findByName(request.getName()).isPresent()) {
            throw new RuntimeException("Tên đơn vị đã tồn tại");
        }

        return unitRepository.save(request);
    }

    // ================= GET ALL (PAGE) =================
    public Page<Unit> getAll(Pageable pageable) {
        return unitRepository.findAll(pageable);
    }

    // ================= GET DETAIL =================
    public Unit getById(Long id) {
        return unitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn vị"));
    }

    // ================= UPDATE =================
    public Unit update(Long id, Unit request) {

        Unit unit = getById(id);

        Optional<Unit> existing = unitRepository.findByName(request.getName());
        if (existing.isPresent() && !existing.get().getId().equals(id)) {
            throw new RuntimeException("Tên đơn vị đã tồn tại");
        }

        unit.setName(request.getName());
        unit.setDescription(request.getDescription());

        return unitRepository.save(unit);
    }

    // ================= DELETE =================
    public void delete(Long id) {

        if (!unitRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy đơn vị");
        }

        unitRepository.deleteById(id);
    }

    // ================= SEARCH =================
    public Page<Unit> search(String keyword, Pageable pageable) {
        return unitRepository.search(keyword, pageable);
    }
}
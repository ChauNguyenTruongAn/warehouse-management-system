package com.github.chaunguyentruongan.warehouse_management_system.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.chaunguyentruongan.warehouse_management_system.models.Unit;
import com.github.chaunguyentruongan.warehouse_management_system.repositories.UnitRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/units")
@RequiredArgsConstructor
public class UnitController {

    private final UnitRepository unitRepository;

    @PostMapping
    public ResponseEntity<?> createUnit(@RequestBody Unit unit) {
        Unit saved = unitRepository.save(unit);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Unit>> getAllUnits() {
        return ResponseEntity.ok(unitRepository.findAll());
    }
}
package com.github.chaunguyentruongan.warehouse_management_system.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.chaunguyentruongan.warehouse_management_system.dto.StockReportDTO;
import com.github.chaunguyentruongan.warehouse_management_system.services.StockReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final StockReportService stockReportService;

    /**
     * API stock report
     */
    @GetMapping("/stock")
    public ResponseEntity<List<StockReportDTO>> getStockReport() {
        List<StockReportDTO> report = stockReportService.getStockReport();
        return ResponseEntity.ok(report);
    }

    @GetMapping("/stock/filter")
    public ResponseEntity<List<StockReportDTO>> getStockLow(
            @RequestParam(defaultValue = "3") int minStock) {

        List<StockReportDTO> all = stockReportService.getStockReport();
        List<StockReportDTO> lowStock = all.stream()
                .filter(r -> r.getStock() <= minStock)
                .toList();

        return ResponseEntity.ok(lowStock);
    }
}
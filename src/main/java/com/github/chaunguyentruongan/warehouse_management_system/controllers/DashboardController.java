package com.github.chaunguyentruongan.warehouse_management_system.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.chaunguyentruongan.warehouse_management_system.dto.ImportExportSummaryDTO;
import com.github.chaunguyentruongan.warehouse_management_system.dto.ReceiptDetailDTO;
import com.github.chaunguyentruongan.warehouse_management_system.dto.StockReportDTO;
import com.github.chaunguyentruongan.warehouse_management_system.models.ExportItem;
import com.github.chaunguyentruongan.warehouse_management_system.models.ImportItem;
import com.github.chaunguyentruongan.warehouse_management_system.repositories.ExportItemRepository;
import com.github.chaunguyentruongan.warehouse_management_system.repositories.ImportItemRepository;
import com.github.chaunguyentruongan.warehouse_management_system.services.DashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final ImportItemRepository importItemRepository;
    private final ExportItemRepository exportItemRepository;

    /**
     * Stock report đầy đủ với cảnh báo tồn kho
     */
    @GetMapping("/stock")
    public ResponseEntity<List<StockReportDTO>> stockReport(
            @RequestParam(defaultValue = "3") int minStock) {

        List<StockReportDTO> report = dashboardService.getStockReport(minStock);
        return ResponseEntity.ok(report);
    }

    /**
     * Phiếu nhập theo ngày
     */
    @GetMapping("/imports")
    public ResponseEntity<List<ImportExportSummaryDTO>> imports(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to) {

        List<ImportExportSummaryDTO> list = dashboardService.getImports(from, to);
        return ResponseEntity.ok(list);
    }

    /**
     * Phiếu xuất theo ngày
     */
    @GetMapping("/exports")
    public ResponseEntity<List<ImportExportSummaryDTO>> exports(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to) {

        List<ImportExportSummaryDTO> list = dashboardService.getExports(from, to);
        return ResponseEntity.ok(list);
    }

    // Controller
    @GetMapping("/imports/{id}/detail")
    public ResponseEntity<List<ReceiptDetailDTO>> importDetail(@PathVariable Long id) {
        List<ImportItem> items = importItemRepository.findItemsByReceiptId(id);
        List<ReceiptDetailDTO> details = items.stream().map(i -> new ReceiptDetailDTO(i.getMaterial().getName(),
                i.getMaterial().getUnit().getName(), i.getQuantity())).toList();
        return ResponseEntity.ok(details);
    }

    @GetMapping("/exports/{id}/detail")
    public ResponseEntity<List<ReceiptDetailDTO>> exportDetail(@PathVariable Long id) {
        List<ExportItem> items = exportItemRepository.findItemsByReceiptId(id);
        List<ReceiptDetailDTO> details = items.stream().map(e -> new ReceiptDetailDTO(e.getMaterial().getName(),
                e.getMaterial().getUnit().getName(), e.getQuantity())).toList();
        return ResponseEntity.ok(details);
    }
}
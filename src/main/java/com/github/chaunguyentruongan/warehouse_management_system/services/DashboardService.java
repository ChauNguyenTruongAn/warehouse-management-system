package com.github.chaunguyentruongan.warehouse_management_system.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.chaunguyentruongan.warehouse_management_system.dto.ImportExportSummaryDTO;
import com.github.chaunguyentruongan.warehouse_management_system.dto.StockReportDTO;
import com.github.chaunguyentruongan.warehouse_management_system.models.Material;
import com.github.chaunguyentruongan.warehouse_management_system.models.StockBalance;
import com.github.chaunguyentruongan.warehouse_management_system.repositories.ExportReceiptRepository;
import com.github.chaunguyentruongan.warehouse_management_system.repositories.ImportReceiptRepository;
import com.github.chaunguyentruongan.warehouse_management_system.repositories.MaterialRepository;
import com.github.chaunguyentruongan.warehouse_management_system.repositories.StockBalanceRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final MaterialRepository materialRepository;
    private final StockBalanceRepository stockBalanceRepository;
    private final ImportReceiptRepository importReceiptRepository;
    private final ExportReceiptRepository exportReceiptRepository;

    /**
     * Stock report chuyên nghiệp với cảnh báo lowStock
     */
    @Transactional
    public List<StockReportDTO> getStockReport(int lowStockThreshold) {

        List<Material> materials = materialRepository.findAll();
        List<StockReportDTO> result = new ArrayList<>();

        for (Material m : materials) {
            int stock = stockBalanceRepository.findById(m.getId())
                    .map(StockBalance::getQuantity)
                    .orElse(0);

            // Total import
            Integer totalImport = importReceiptRepository.sumQuantityByMaterialId(m.getId());
            if (totalImport == null)
                totalImport = 0;

            // Total export
            Integer totalExport = exportReceiptRepository.sumQuantityByMaterialId(m.getId());
            if (totalExport == null)
                totalExport = 0;

            boolean lowStock = stock <= lowStockThreshold;

            result.add(new StockReportDTO(
                    m.getId(),
                    m.getName(),
                    m.getUnit().getName(),
                    totalImport,
                    totalExport,
                    stock,
                    lowStock));
        }

        return result;
    }

    /**
     * Lấy phiếu nhập theo ngày
     */
    public List<ImportExportSummaryDTO> getImports(LocalDate from, LocalDate to) {
        return importReceiptRepository.findAllByImportDateBetween(from, to)
                .stream()
                .map(r -> new ImportExportSummaryDTO(
                        r.getId(),
                        r.getCode(),
                        r.getImportDate(),
                        r.getItems().size()))
                .toList();
    }

    /**
     * Lấy phiếu xuất theo ngày
     */
    public List<ImportExportSummaryDTO> getExports(LocalDate from, LocalDate to) {
        return exportReceiptRepository.findAllByExportDateBetween(from, to)
                .stream()
                .map(r -> new ImportExportSummaryDTO(
                        r.getId(),
                        r.getCode(),
                        r.getExportDate(),
                        r.getItems().size()))
                .toList();
    }
}
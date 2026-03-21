package com.github.chaunguyentruongan.warehouse_management_system.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.chaunguyentruongan.warehouse_management_system.dto.StockReportDTO;
import com.github.chaunguyentruongan.warehouse_management_system.models.Material;
import com.github.chaunguyentruongan.warehouse_management_system.models.StockBalance;
import com.github.chaunguyentruongan.warehouse_management_system.repositories.MaterialRepository;
import com.github.chaunguyentruongan.warehouse_management_system.repositories.StockBalanceRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockReportService {

    private final MaterialRepository materialRepository;
    private final StockBalanceRepository stockBalanceRepository;

    /**
     * Lấy stock report list
     */
    @Transactional
    public List<StockReportDTO> getStockReport() {
        List<Material> materials = materialRepository.findAll();
        List<StockReportDTO> result = new ArrayList<>();

        for (Material m : materials) {
            int stock = stockBalanceRepository.findById(m.getId())
                    .map(StockBalance::getQuantity)
                    .orElse(0);

            int totalImport = stock; // vì stock_balance = total_import - total_export
            int totalExport = 0; // Có thể query sum export_item nếu muốn chi tiết

            result.add(new StockReportDTO(
                    m.getId(),
                    m.getName(),
                    m.getUnit().getName(),
                    totalImport,
                    totalExport,
                    stock));
        }

        return result;
    }
}
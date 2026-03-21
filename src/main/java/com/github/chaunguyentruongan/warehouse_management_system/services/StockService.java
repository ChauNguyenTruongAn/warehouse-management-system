package com.github.chaunguyentruongan.warehouse_management_system.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.chaunguyentruongan.warehouse_management_system.dto.StockDTO;
import com.github.chaunguyentruongan.warehouse_management_system.models.Material;
import com.github.chaunguyentruongan.warehouse_management_system.models.StockBalance;
import com.github.chaunguyentruongan.warehouse_management_system.repositories.MaterialRepository;
import com.github.chaunguyentruongan.warehouse_management_system.repositories.StockBalanceRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final MaterialRepository materialRepository;
    private final StockBalanceRepository stockBalanceRepository;

    /**
     * Cập nhật stock khi import (+) hoặc export (-)
     */
    @Transactional
    public void updateStock(Long materialId, int quantityChange) {
        // lock row để tránh race condition
        StockBalance stockBalance = stockBalanceRepository
                .findByMaterialIdForUpdate(materialId)
                .orElseGet(() -> {
                    StockBalance s = new StockBalance();
                    s.setMaterialId(materialId);
                    s.setQuantity(0);
                    return s;
                });

        int newQuantity = stockBalance.getQuantity() + quantityChange;

        if (newQuantity < 0) {
            throw new RuntimeException("Not enough stock for materialId: " + materialId);
        }

        stockBalance.setQuantity(newQuantity);
        stockBalance.setUpdatedAt(LocalDateTime.now());

        stockBalanceRepository.save(stockBalance);
    }

    /**
     * Lấy stock hiện tại cho một material
     */
    @Transactional(readOnly = true)
    public int getStock(Long materialId) {
        return stockBalanceRepository.findById(materialId)
                .map(StockBalance::getQuantity)
                .orElse(0);
    }

    /**
     * Lấy toàn bộ stock list (dùng cho dashboard / report)
     */
    @Transactional(readOnly = true)
    public List<StockDTO> getAllStock() {
        List<Material> materials = materialRepository.findAll();
        List<StockDTO> result = new ArrayList<>();

        for (Material m : materials) {
            int qty = stockBalanceRepository.findById(m.getId())
                    .map(StockBalance::getQuantity)
                    .orElse(0);
            result.add(new StockDTO(m.getId(), m.getName(), m.getUnit().getName(), qty));
        }

        return result;
    }
}
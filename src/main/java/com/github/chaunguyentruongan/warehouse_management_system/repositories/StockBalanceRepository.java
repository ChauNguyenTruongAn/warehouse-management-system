package com.github.chaunguyentruongan.warehouse_management_system.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.github.chaunguyentruongan.warehouse_management_system.models.StockBalance;

import jakarta.persistence.LockModeType;

@Repository
public interface StockBalanceRepository extends JpaRepository<StockBalance, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM StockBalance s WHERE s.materialId = :materialId")
    Optional<StockBalance> findByMaterialIdForUpdate(Long materialId);
}
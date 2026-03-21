package com.github.chaunguyentruongan.warehouse_management_system.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_balance")
@Getter
@Setter
public class StockBalance {

    @Id
    private Long materialId;

    private Integer quantity;

    private LocalDateTime updatedAt;
}
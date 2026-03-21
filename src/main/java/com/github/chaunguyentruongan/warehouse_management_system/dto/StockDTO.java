package com.github.chaunguyentruongan.warehouse_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockDTO {
    private Long materialId;
    private String materialName;
    private String unitName;
    private Integer stock;
}
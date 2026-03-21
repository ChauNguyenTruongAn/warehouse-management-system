package com.github.chaunguyentruongan.warehouse_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockReportDTO {
    private Long materialId;
    private String materialName;
    private String unitName;
    private Integer totalImport;
    private Integer totalExport;
    private Integer stock;
}
package com.github.chaunguyentruongan.warehouse_management_system.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImportExportSummaryDTO {
    private Long id;
    private String code;
    private LocalDate date;
    private int totalItems;
}

package com.github.chaunguyentruongan.warehouse_management_system.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class ImportRequest {
    private LocalDate importDate;
    private String note;
    private List<ImportItemRequest> items;
}

package com.github.chaunguyentruongan.warehouse_management_system.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class ExportRequest {
    private LocalDate exportDate;
    private String recipient;
    private String department;
    private String purpose;
    private String note;
    private List<ExportItemRequest> items;
}
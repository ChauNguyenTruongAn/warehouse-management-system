package com.github.chaunguyentruongan.warehouse_management_system.dto;

import lombok.Data;

@Data
public class ImportItemRequest {
    private Long materialId;
    private Integer quantity;
}
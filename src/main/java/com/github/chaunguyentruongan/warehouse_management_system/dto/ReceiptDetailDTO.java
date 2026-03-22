package com.github.chaunguyentruongan.warehouse_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// DTO chi tiết
@Data
@AllArgsConstructor
public class ReceiptDetailDTO {
    private String materialName;
    private String unitName;
    private int quantity;
}

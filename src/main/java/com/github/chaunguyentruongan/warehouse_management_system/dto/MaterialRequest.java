package com.github.chaunguyentruongan.warehouse_management_system.dto;

import lombok.Data;

@Data
public class MaterialRequest {
    private String code;
    private String name;
    private Long unitId;
    private Integer minStock;
    private String description;
}
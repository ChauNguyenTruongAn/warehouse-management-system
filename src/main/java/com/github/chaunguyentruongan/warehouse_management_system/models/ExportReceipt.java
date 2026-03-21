package com.github.chaunguyentruongan.warehouse_management_system.models;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "export_receipt")
@Getter
@Setter
public class ExportReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private LocalDate exportDate;

    private String recipient;

    private String department;

    private String purpose;

    private String note;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL)
    private List<ExportItem> items;
}
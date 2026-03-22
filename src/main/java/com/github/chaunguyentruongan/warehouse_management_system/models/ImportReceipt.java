package com.github.chaunguyentruongan.warehouse_management_system.models;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "import_receipt")
@Getter
@Setter
public class ImportReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;

    private LocalDate importDate;

    private String note;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL)
    private List<ImportItem> items;
}
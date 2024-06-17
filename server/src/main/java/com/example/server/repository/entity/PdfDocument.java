package com.example.server.repository.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class PdfDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    private String fileName;

    @Lob
    private byte[] data;

    @ManyToOne
    @JoinColumn(name="customer_id")
    @JsonBackReference(value = "pdfDocuments-customer")
    private Customer customer;
}

package com.example.server.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class FormSignedByCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long signedById;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "form_id")
    private Form form;

    private LocalDateTime timestamp;
}

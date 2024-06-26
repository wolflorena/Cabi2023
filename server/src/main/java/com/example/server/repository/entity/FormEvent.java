package com.example.server.repository.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FormEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long formEventId;

    @Column(name = "form_event_type")
    @Enumerated(EnumType.STRING)
    private FormEventType formEventType;

    @Column(name = "timestamp")
    private LocalDate timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id")
    @JsonBackReference(value = "formEvents-form")
    private Form form;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonBackReference(value = "formEvents-customer")
    private Customer customer;

    public enum FormEventType {
        VIEWED, SIGNED, NEW
    }
}

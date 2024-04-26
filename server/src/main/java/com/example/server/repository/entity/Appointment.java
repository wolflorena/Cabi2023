package com.example.server.repository.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Appointment {

    public enum AppointmentStatus {
        REQUESTED,
        SCHEDULED,
        COMPLETED,
        CANCELLED
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;
    private LocalDate date;
    private LocalTime time;
    private int finalDuration;

    // Enum type for status, e.g., SCHEDULED, CANCELLED, COMPLETED
    // Assuming you will create an Enum called AppointmentStatus
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonBackReference(value="appointments-customer")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    @JsonBackReference(value="appointments-doctor")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;
}
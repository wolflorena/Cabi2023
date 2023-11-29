package com.example.server.repository.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
public class Appointment {

    public enum AppointmentStatus {
        SCHEDULED,
        CANCELLED,
        COMPLETED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;

    private LocalDate dateAndTime;  // Consider splitting into two fields if needed: one for LocalDate and one for LocalTime
    private LocalTime finalDuration;

    // Enum type for status, e.g., SCHEDULED, CANCELLED, COMPLETED
    // Assuming you will create an Enum called AppointmentStatus
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;


    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

}
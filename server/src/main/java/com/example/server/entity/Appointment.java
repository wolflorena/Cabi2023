package com.example.server.entity;

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
    private LocalTime duration;

    // Enum type for status, e.g., SCHEDULED, CANCELLED, COMPLETED
    // Assuming you will create an Enum called AppointmentStatus
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    // This will be linked to a Customer
    // private Customer customer;

    // This will be linked to a Doctor
    // private Doctor doctor;

    // This will be linked to a Service
    // private Service service;
}
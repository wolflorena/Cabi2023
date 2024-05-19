package com.example.server.repository.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class AppointmentDoctorDashboardDTO {
    private Long appointmentId;
    private LocalDate date;
    private LocalTime time;
    private String customerFirstName;
    private String customerLastName;
    private String serviceName;
}

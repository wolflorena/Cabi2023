package com.example.server.repository.DTOs;

import com.example.server.repository.entity.Appointment.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class AppointmentResponseDTO {
    private Long appointmentId;
    private LocalDate date;
    private LocalTime time;
    private int finalDuration;
    private AppointmentStatus status;
    private Long customerId;
    private String customerFirstName;
    private String customerLastName;
    private Long doctorId;
    private String doctorFirstName;
    private String doctorLastName;
    private String serviceId;
    private String serviceName;
}

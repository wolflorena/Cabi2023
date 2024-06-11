package com.example.server.repository.DTOs;

import com.example.server.repository.entity.Appointment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class AppointmentHistoryDTO {
    private Long appointmentId;
    private LocalDate date;
    private LocalTime time;
    private Appointment.AppointmentStatus status;
    private String serviceName;
    private String doctorFirstName;
    private String doctorLastName;
}

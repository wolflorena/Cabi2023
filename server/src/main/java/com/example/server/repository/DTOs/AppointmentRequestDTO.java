package com.example.server.repository.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class AppointmentRequestDTO {
    private LocalDate date;
    private LocalTime time;
    private Long doctorId;
    private Long serviceId;
}

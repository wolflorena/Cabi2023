package com.example.server.repository.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAppointmentDTO {
    private LocalDate date;
    private LocalTime time;
    private Long doctorId;
    private Long serviceId;
}

package com.example.server.repository.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
public class AppointmentTimeDTO {
    private LocalTime time;
    private int totalDuration;
}

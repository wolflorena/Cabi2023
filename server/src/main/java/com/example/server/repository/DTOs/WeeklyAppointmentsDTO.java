package com.example.server.repository.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyAppointmentsDTO {
    private String week;
    private Long count;
}

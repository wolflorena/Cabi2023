package com.example.server.repository.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentPageDTO {
    private int total;
    private Page<AppointmentAdminDTO> pagedAppointments;
}

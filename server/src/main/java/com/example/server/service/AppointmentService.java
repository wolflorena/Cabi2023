package com.example.server.service;

import com.example.server.repository.DTOs.AppointmentRequestDTO;
import com.example.server.repository.DTOs.AppointmentResponseDTO;

public interface AppointmentService {
    public AppointmentResponseDTO addAppointment(AppointmentRequestDTO appointmentRequestDTO, Long customerId);
}

package com.example.server.service;

import com.example.server.repository.DTOs.AppointmentRequestDTO;
import com.example.server.repository.DTOs.AppointmentResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {
    AppointmentResponseDTO addAppointment(AppointmentRequestDTO appointmentRequestDTO, Long customerId);
    AppointmentResponseDTO getAppointmentById(Long appointmentId);

    List<AppointmentResponseDTO> getAllAppointments();
    List<AppointmentResponseDTO> getAllAppointmentsByDateAndDoctor(LocalDate date, Long doctorId);
}

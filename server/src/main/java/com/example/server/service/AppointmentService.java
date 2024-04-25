package com.example.server.service;

import com.example.server.repository.DTOs.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {
    AppointmentResponseDTO addAppointment(AppointmentRequestDTO appointmentRequestDTO, Long customerId);
    AppointmentDetailDTO getAppointmentById(Long appointmentId);

    Boolean deleteAppointment(Long appointmentId);

    List<AppointmentResponseDTO> getAllAppointments();

    List<AppointmentCalendarDTO> getAllAppointmentsForCalendar();

    AppointmentPageDTO getAllAppointmentsForAdmin(Pageable pageable, List<Long> doctorIds);
    List<AppointmentResponseDTO> getAllAppointmentsByDateAndDoctor(LocalDate date, Long doctorId);
}

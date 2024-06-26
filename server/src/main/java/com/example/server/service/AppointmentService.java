package com.example.server.service;

import com.example.server.repository.DTOs.*;
import com.example.server.repository.entity.Appointment;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {
    AppointmentResponseDTO addAppointment(AppointmentRequestDTO appointmentRequestDTO);
    AppointmentDetailDTO getAppointmentById(Long appointmentId);

    Boolean deleteAppointment(Long appointmentId);

    List<AppointmentResponseDTO> getAllAppointments();

    List<AppointmentCalendarDTO> getAllAppointmentsForCalendar();

    AppointmentPageDTO getAllAppointmentsForAdmin(Pageable pageable, List<Long> doctorIds, Appointment.AppointmentStatus status);
    List<AppointmentResponseDTO> getAllAppointmentsByDateAndDoctor(LocalDate date, Long doctorId);
    AppointmentResponseDTO updateAppointmentStatus(Long appointmentId, Appointment.AppointmentStatus status);
    List<AppointmentDoctorDashboardDTO> getNextAppointments(Long doctorId);
    int getAppointmentsNumberByStatusAndDoctor(Long doctorId, Appointment.AppointmentStatus appointmentStatus);
    List<WeeklyAppointmentsDTO> getWeeklyAppointments(Long doctorId);
    HistoryPageDTO getHistoryByCustomerId(Long customerId, Pageable pageable);
    List<AppointmentResponseDTO> getAppointmentsByDoctorAndViewType(Long doctorId, LocalDate date, String viewType);
    AppointmentResponseDTO rescheduleAppointment(Long appointmentId, UpdateAppointmentDTO updateAppointmentDTO);
}

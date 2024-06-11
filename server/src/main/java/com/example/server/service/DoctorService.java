package com.example.server.service;

import com.example.server.repository.DTOs.*;
import com.example.server.repository.DTOs.Customers.ChangePasswordBodyDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface DoctorService {
    DoctorResponseDTO addDoctor(DoctorAdminRequestDTO doctorAdminRequestDTO);
    DoctorResponseDTO getDoctorById(Long doctorId);
    List<DoctorResponseDTO> getAll();
    List<LocalDate> findAvailableDates(Long doctorId, Long serviceId);
    List<LocalTime> findAvailableHours(Long doctorId, Long serviceId, LocalDate date);
    DoctorUpdateDTO updateDoctor(Long doctorId, DoctorUpdateDTO doctorUpdateDTO);
    List<TreatmentTypesDTO> getDoctorServicesInCurrentMonth(Long doctorId);
    boolean deleteDoctorById(Long doctorId);
    boolean changePassword(Long doctorId, String currentPassword, String newPassword);
}

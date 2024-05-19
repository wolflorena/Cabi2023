package com.example.server.service;

import com.example.server.repository.DTOs.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface DoctorService {
    DoctorResponseDTO addDoctor(DoctorRequestDTO doctorRequestDTO);
    DoctorResponseDTO getDoctorById(Long doctorId);
    List<DoctorResponseDTO> getAll();
    List<LocalDate> findAvailableDates(Long doctorId, Long serviceId);
    List<LocalTime> findAvailableHours(Long doctorId, Long serviceId, LocalDate date);
    DoctorUpdateDTO updateDoctor(Long doctorId, DoctorUpdateDTO doctorUpdateDTO);
    List<TreatmentTypesDTO> getDoctorServicesInCurrentMonth(Long doctorId);
}

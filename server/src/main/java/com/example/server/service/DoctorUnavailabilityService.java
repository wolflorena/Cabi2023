package com.example.server.service;

import com.example.server.repository.DTOs.VacationRequestDTO;
import com.example.server.repository.entity.DoctorUnavailability;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

public interface DoctorUnavailabilityService {
    List<DoctorUnavailability> getUnavailabilitiesByDoctorId(Long doctorId);
    List<VacationRequestDTO> getAllVacationsForCalendar();
    List<VacationRequestDTO> getAllVacationsForCalendarForYear(Year year);
    VacationRequestDTO createUnavailability(VacationRequestDTO unavailability);
    void deleteUnavailability(Long id);
}

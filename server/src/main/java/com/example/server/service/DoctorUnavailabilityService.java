package com.example.server.service;

import com.example.server.repository.DTOs.VacationRequestDTO;
import com.example.server.repository.entity.DoctorUnavailability;

import java.util.List;

public interface DoctorUnavailabilityService {
    List<DoctorUnavailability> getUnavailabilitiesByDoctorId(Long doctorId);
    VacationRequestDTO createUnavailability(VacationRequestDTO unavailability);
    void deleteUnavailability(Long id);
}

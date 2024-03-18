package com.example.server.service;

import com.example.server.repository.DTOs.DoctorRequestDTO;
import com.example.server.repository.DTOs.DoctorResponseDTO;

import java.util.List;

public interface DoctorService {
    DoctorResponseDTO addDoctor(DoctorRequestDTO doctorRequestDTO);
    DoctorResponseDTO getDoctorById(Long doctorId);
    List<DoctorResponseDTO> getAll();
}

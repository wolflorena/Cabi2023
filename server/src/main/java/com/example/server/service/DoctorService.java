package com.example.server.service;

import com.example.server.repository.DTOs.DoctorRequestDTO;
import com.example.server.repository.DTOs.DoctorResponseDTO;

public interface DoctorService {
    public DoctorResponseDTO addDoctor(DoctorRequestDTO doctorRequestDTO);
}

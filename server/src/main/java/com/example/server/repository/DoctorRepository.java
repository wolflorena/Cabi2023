package com.example.server.repository;

import com.example.server.repository.DTOs.AppointmentTimeDTO;
import com.example.server.repository.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByEmail(String email);
    Optional<Doctor> findById(Long id);
}

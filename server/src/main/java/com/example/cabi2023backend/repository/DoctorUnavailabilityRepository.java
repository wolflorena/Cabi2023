package com.example.cabi2023backend.repository;

import com.example.cabi2023backend.entity.DoctorUnavailability;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorUnavailabilityRepository extends JpaRepository<DoctorUnavailability, Long> {
}

package com.example.server.repository;

import com.example.server.repository.entity.DoctorUnavailability;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorUnavailabilityRepository extends JpaRepository<DoctorUnavailability, Long> {
}

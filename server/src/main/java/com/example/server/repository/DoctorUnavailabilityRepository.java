package com.example.server.repository;

import com.example.server.entity.DoctorUnavailability;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorUnavailabilityRepository extends JpaRepository<DoctorUnavailability, Long> {
}

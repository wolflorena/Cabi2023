package com.example.server.repository;

import com.example.server.repository.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsByDateAndTimeAndDoctorId(LocalDate date,
                                           LocalTime time,
                                           Long doctorId);
    List<Appointment> findByDateAndDoctorId(LocalDate date, Long doctorId);
}

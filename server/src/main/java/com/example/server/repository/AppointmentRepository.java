package com.example.server.repository;

import com.example.server.repository.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsByDateAndTimeAndDoctorId(LocalDate date,
                                           LocalTime time,
                                           Long doctorId);
    List<Appointment> findByDateAndDoctorId(LocalDate date, Long doctorId);

    Page<Appointment> findAll(Pageable pageable);

    Page<Appointment> findAllByStatus(Appointment.AppointmentStatus status, Pageable pageable);
    Page<Appointment> findAllByDoctorIdIn(List<Long> doctorIds, Pageable pageable);
    Page<Appointment> findAllByDoctorIdInAndStatus(List<Long> doctorIds, Appointment.AppointmentStatus status, Pageable pageable);

    @Query("SELECT a FROM Appointment a WHERE (a.date < :today OR (a.date = :today AND a.time <= :now)) AND a.status = 'SCHEDULED'")
    List<Appointment> findPastDueAppointments(LocalDate today, LocalTime now);

}

package com.example.server.repository;

import com.example.server.repository.DTOs.AppointmentTimeDTO;
import com.example.server.repository.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsByDateAndTimeAndDoctorIdAndStatusNot(LocalDate date,
                                                       LocalTime time,
                                                       Long doctorId,
                                                       Appointment.AppointmentStatus status);
    List<Appointment> findByDateAndDoctorId(LocalDate date, Long doctorId);

    Page<Appointment> findAll(Pageable pageable);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.date BETWEEN :startDate AND :endDate")
    List<Appointment> findByDoctorIdAndDateRange(Long doctorId, LocalDate startDate, LocalDate endDate);
    Page<Appointment> findAllByStatusIn(List<Appointment.AppointmentStatus> statuses, Pageable pageable);
    Page<Appointment> findAllByDoctorIdIn(List<Long> doctorIds, Pageable pageable);
    Page<Appointment> findAllByDoctorIdInAndStatusIn(List<Long> doctorIds, List<Appointment.AppointmentStatus> statuses, Pageable pageable);
    @Query("SELECT a FROM Appointment a WHERE (a.date < :today OR (a.date = :today AND a.time <= :now)) AND a.status = 'SCHEDULED'")
    List<Appointment> findPastDueAppointments(LocalDate today, LocalTime now);
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.date = :date AND a.status != 'CANCELLED'")
    List<Appointment> findBookedTimesAndDurationsByDoctorIdAndDate(Long doctorId, LocalDate date);
    List<Appointment> findByDoctorIdAndDateBetween(Long doctorId, LocalDate startOfMonth, LocalDate endOfMonth);
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.date = :date AND a.time > :time AND a.status = 'SCHEDULED'")
    Optional<List<Appointment>> findNextAppointmentsByDoctorId(Long doctorId, LocalDate date, LocalTime time);
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.status = :status " +
            "AND a.date >= :startOfMonth AND a.date <= :endOfMonth")
    List<Appointment> findAllByDoctorIdAndStatusAndDateBetween(@Param("doctorId") Long doctorId,
                                                               @Param("status") Appointment.AppointmentStatus status,
                                                               @Param("startOfMonth") LocalDate startOfMonth,
                                                               @Param("endOfMonth") LocalDate endOfMonth);

    List<Appointment> findBookedTimesAndDurationsByDoctorIdAndDateAndStatusNot(Long doctorId,
                                                                               LocalDate date,
                                                                               Appointment.AppointmentStatus status);
    @Query("SELECT a FROM Appointment a WHERE a.status IN (:statuses)")
    List<Appointment> findAllByStatusIn(@Param("statuses") List<Appointment.AppointmentStatus> statuses);

    List<Appointment> findByCustomerId(@Param("customerId") Long customerId);
    @Query("SELECT a FROM Appointment a WHERE a.customer.id = :customerId ORDER BY a.date DESC, a.time DESC")
    Page<Appointment> findAllByCustomerIdOrderByDateTimeDesc(@Param("customerId") Long customerId, Pageable pageable);
}

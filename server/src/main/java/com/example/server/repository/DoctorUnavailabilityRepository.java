package com.example.server.repository;

import com.example.server.repository.entity.DoctorUnavailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface DoctorUnavailabilityRepository extends JpaRepository<DoctorUnavailability, Long> {
    List<DoctorUnavailability> findByDoctorId(Long doctorId);
    boolean existsByStartDateAndEndDateAndDoctorId(LocalDate startDate,
                                                   LocalDate endDate,
                                           Long doctorId);
    boolean existsByStartTimeAndEndTimeAndStartDateAndDoctorId(LocalTime startTime,
                                                   LocalTime endTime,
                                                   LocalDate startDate,
                                                   Long doctorId);

    @Query("SELECT du FROM DoctorUnavailability du WHERE du.doctor.id = :doctorId AND :date BETWEEN du.startDate AND du.endDate")
    List<DoctorUnavailability> findByDoctorIdAndDate(@Param("doctorId") Long doctorId, @Param("date") LocalDate date);
}

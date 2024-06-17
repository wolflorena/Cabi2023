package com.example.server.repository;

import com.example.server.repository.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmail(String email);
    Optional<Customer> findByEmail(String email);
    boolean existsByPhoneNo(String PhoneNo);
    Optional<Customer> findById(Long id);
    @Query("SELECT DISTINCT c FROM Customer c JOIN c.appointments a WHERE a.doctor.id = :doctorId AND a.status IN ('SCHEDULED', 'COMPLETED')")
    List<Customer> findCustomersByDoctorIdAndAppointmentStatus(@Param("doctorId") Long doctorId);
    @Query("SELECT c FROM Customer c WHERE c.accountStatus != :accountStatus")
    Page<Customer> findAllByStatusNot(Customer.AccountStatus accountStatus, Pageable pageable);
}

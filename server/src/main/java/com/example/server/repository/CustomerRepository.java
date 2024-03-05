package com.example.server.repository;

import com.example.server.repository.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmail(String email);

    boolean existsByPhoneNo(String PhoneNo);
    Optional<Customer> findById(Long id);
}

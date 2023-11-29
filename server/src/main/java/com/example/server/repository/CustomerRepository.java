package com.example.server.repository;

import com.example.server.repository.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmail(String email);

    boolean existsByPhoneNo(String PhoneNo);
}

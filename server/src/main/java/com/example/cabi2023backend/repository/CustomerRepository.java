package com.example.cabi2023backend.repository;

import com.example.cabi2023backend.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}

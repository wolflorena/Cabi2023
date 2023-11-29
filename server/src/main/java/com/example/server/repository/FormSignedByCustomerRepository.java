package com.example.server.repository;

import com.example.server.repository.entity.FormSignedByCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormSignedByCustomerRepository extends JpaRepository<FormSignedByCustomer, Long> {
}

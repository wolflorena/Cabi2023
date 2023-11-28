package com.example.server.repository;

import com.example.server.entity.FormSeenByCustomer;
import com.example.server.entity.FormSignedByCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormSeenByCustomerRepository extends JpaRepository<FormSeenByCustomer, Long> {
}

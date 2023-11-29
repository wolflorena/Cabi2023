package com.example.server.repository;

import com.example.server.repository.entity.FormSeenByCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormSeenByCustomerRepository extends JpaRepository<FormSeenByCustomer, Long> {
}

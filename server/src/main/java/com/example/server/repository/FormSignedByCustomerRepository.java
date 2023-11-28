package com.example.server.repository;

import com.example.server.entity.Form;
import com.example.server.entity.FormSignedByCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormSignedByCustomerRepository extends JpaRepository<FormSignedByCustomer, Long> {
}

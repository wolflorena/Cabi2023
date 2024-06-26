package com.example.server.repository;

import com.example.server.repository.entity.Form;
import com.example.server.repository.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FormRepository extends JpaRepository<Form, Long> {
    Optional<Form> findByTitle(String title);
}

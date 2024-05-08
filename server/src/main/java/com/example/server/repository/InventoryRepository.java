package com.example.server.repository;

import com.example.server.repository.entity.Appointment;
import com.example.server.repository.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Page<Inventory> findAll(Pageable pageable);
    Optional<Inventory> findByProduct(String product);
}

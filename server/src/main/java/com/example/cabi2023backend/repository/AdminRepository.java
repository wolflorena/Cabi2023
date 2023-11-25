package com.example.cabi2023backend.repository;

import com.example.cabi2023backend.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}

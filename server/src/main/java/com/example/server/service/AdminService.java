package com.example.server.service;

import com.example.server.repository.DTOs.AdminResponseDTO;

public interface AdminService {
    AdminResponseDTO createAdmin(String email, String password);
}

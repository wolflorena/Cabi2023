package com.example.server.controller;

import com.example.server.repository.DTOs.AdminRequestDTO;
import com.example.server.repository.DTOs.AdminResponseDTO;
import com.example.server.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping
    public ResponseEntity<AdminResponseDTO> createAdmin(@RequestParam(name="email") String email, @RequestParam(name="password") String password) {
        return new ResponseEntity<>(adminService.createAdmin(email,password), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public AdminResponseDTO login(@RequestBody AdminRequestDTO adminRequestDTO) {
        return adminService.loginAdmin(adminRequestDTO.getEmail(), adminRequestDTO.getPassword());
    }
}

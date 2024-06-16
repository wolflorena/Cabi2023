package com.example.server.service.implementation;

import com.example.server.exception.types.EmailExistsException;
import com.example.server.repository.*;
import com.example.server.repository.DTOs.AdminResponseDTO;
import com.example.server.repository.entity.Admin;
import com.example.server.service.AdminService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(AdminRepository adminRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AdminResponseDTO createAdmin(String email, String password) {
        Optional<Admin> optionalAdmin = adminRepository.findByEmail(email);
        if(optionalAdmin.isPresent()) {
            throw new EmailExistsException("Email already exists");
        }
        Admin admin = new Admin();
        admin.setEmail(email);
        String encodedPassword = passwordEncoder.encode(password);
        admin.setPassword(encodedPassword);
        adminRepository.save(admin);
        return modelMapper.map(admin, AdminResponseDTO.class);
    }
    @Override
    public AdminResponseDTO loginAdmin(String email, String password) {
        Optional<Admin> optionalAdmin = adminRepository.findByEmail(email);
        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();
            if (password.equals(admin.getPassword())) {
                return modelMapper.map(
                        admin,
                        AdminResponseDTO.class
                );
            }
        }
        throw new BadCredentialsException("Bad credentials");
    }
}

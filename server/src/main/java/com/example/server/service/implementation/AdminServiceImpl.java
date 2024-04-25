package com.example.server.service.implementation;

import com.example.server.exception.types.EmailExistsException;
import com.example.server.repository.*;
import com.example.server.repository.DTOs.AdminResponseDTO;
import com.example.server.repository.entity.Admin;
import com.example.server.service.AdminService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final ModelMapper modelMapper;

    private AdminServiceImpl(AdminRepository adminRepository, ModelMapper modelMapper){
        this.adminRepository = adminRepository;
        this.modelMapper = modelMapper;
    }
    @Override
    public AdminResponseDTO createAdmin(String email, String password) {
        if(adminRepository.findByEmail(email) != null) {
            throw new EmailExistsException("Email already exists");
        }
        Admin admin = new Admin();
        admin.setEmail(email);
        admin.setPassword(password);
        adminRepository.save(admin);
        return modelMapper.map(admin, AdminResponseDTO.class);
    }
    @Override
    public AdminResponseDTO loginAdmin(String email, String password) {
        Admin admin = adminRepository.findByEmail(email);
        if (admin != null) {
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

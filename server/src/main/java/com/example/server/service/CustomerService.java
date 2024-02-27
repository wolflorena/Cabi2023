package com.example.server.service;

import com.example.server.repository.DTOs.RegisterCustomerDTO;
import com.example.server.repository.DTOs.ResponseCustomerDTO;

import java.util.List;

public interface CustomerService {

    public ResponseCustomerDTO register(RegisterCustomerDTO registerCustomerDTO);
    public ResponseCustomerDTO getById(Long customerId);
    public List<ResponseCustomerDTO> getAll();
    public void deleteById(Long customerId);
}

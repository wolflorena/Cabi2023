package com.example.server.service;

import com.example.server.repository.DTOs.Customers.CustomerEditDetailsDTO;
import com.example.server.repository.DTOs.Customers.CustomerPageDTO;
import com.example.server.repository.DTOs.Customers.RegisterCustomerDTO;
import com.example.server.repository.DTOs.Customers.ResponseCustomerDTO;
import com.example.server.repository.entity.Customer;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {

    public ResponseCustomerDTO register(RegisterCustomerDTO registerCustomerDTO);
    public ResponseCustomerDTO getById(Long customerId);
    public List<ResponseCustomerDTO> getAll();
    public void deleteById(Long customerId);
    CustomerPageDTO getAllCustomersForAdmin(Pageable pageable);
    CustomerPageDTO getAllCustomersForDoctor(Pageable pageable, Long doctorId);
    ResponseCustomerDTO editAccountStatus(Long customerId, Customer.AccountStatus status);
    CustomerEditDetailsDTO editCustomerDetails(Long customerId, CustomerEditDetailsDTO customer);
}

package com.example.server.service.implementation;

import com.example.server.repository.DTOs.RegisterCustomerDto;
import com.example.server.repository.entity.Customer;
import com.example.server.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer save(RegisterCustomerDto registerCustomerDto){
        Customer customerToBeSaved = new Customer(registerCustomerDto);
        return customerRepository.save(customerToBeSaved);
    }

    public Customer findById(Long id){
        return customerRepository.findById(id).orElse(null);
    }

    public List<Customer> findAll(){
        return customerRepository.findAll();
    }

    public void deleteById(Long id){
        customerRepository.deleteById(id);
    }

    public boolean emailExists(String email) {
        String lowerCaseEmail = email.toLowerCase();
        return customerRepository.existsByEmail(lowerCaseEmail);
    }
}

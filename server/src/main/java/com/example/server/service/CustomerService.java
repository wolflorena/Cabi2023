package com.example.server.service;

import com.example.server.DTOs.RegisterCustomerDto;
import com.example.server.entity.Customer;
import com.example.server.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
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

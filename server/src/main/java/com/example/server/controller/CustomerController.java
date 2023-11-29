package com.example.server.controller;

import com.example.server.repository.DTOs.RegisterCustomerDto;
import com.example.server.repository.entity.Customer;
import com.example.server.service.CustomerService;
import com.example.server.service.implementation.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerServiceImpl customerServiceImpl;

    public CustomerController(CustomerServiceImpl customerServiceImpl) {
        this.customerServiceImpl = customerServiceImpl;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody RegisterCustomerDto registerCustomerDto){
        registerCustomerDto.setEmail(registerCustomerDto.getEmail().toLowerCase());

        if(customerServiceImpl.emailExists(registerCustomerDto.getEmail())){
            return ResponseEntity.badRequest().body("Email already in use");
        }

        Customer savedCustomer = customerServiceImpl.save(registerCustomerDto);
        if(savedCustomer != null){
            return ResponseEntity.ok().body("Customer successfully registered!");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id){
        Customer retrievedCustomer = customerServiceImpl.findById(id);
        if( retrievedCustomer != null ){
            return ResponseEntity.ok(retrievedCustomer);
        }
        return ResponseEntity.notFound().build();
    }

}

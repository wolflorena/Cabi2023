package com.example.server.controller;

import com.example.server.DTOs.RegisterCustomerDto;
import com.example.server.entity.Customer;
import com.example.server.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Locale;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody RegisterCustomerDto registerCustomerDto){
        registerCustomerDto.setEmail(registerCustomerDto.getEmail().toLowerCase());

        if(customerService.emailExists(registerCustomerDto.getEmail())){
            return ResponseEntity.badRequest().body("Email already in use");
        }

        Customer savedCustomer = customerService.save(registerCustomerDto);
        if(savedCustomer != null){
            return ResponseEntity.ok().body("Customer successfully registered!");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id){
        Customer retrievedCustomer = customerService.findById(id);
        if( retrievedCustomer != null ){
            return ResponseEntity.ok(retrievedCustomer);
        }
        return ResponseEntity.notFound().build();
    }

}

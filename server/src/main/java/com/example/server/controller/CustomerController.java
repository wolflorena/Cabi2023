package com.example.server.controller;

import com.example.server.repository.DTOs.RegisterCustomerDTO;
import com.example.server.repository.DTOs.ResponseCustomerDTO;
import com.example.server.repository.entity.Customer;
import com.example.server.service.implementation.CustomerServiceImpl;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerServiceImpl customerServiceImpl;

    public CustomerController(CustomerServiceImpl customerServiceImpl) {
        this.customerServiceImpl = customerServiceImpl;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterCustomerDTO registerCustomerDto){
        registerCustomerDto.setEmail(registerCustomerDto.getEmail().toLowerCase());

        if(customerServiceImpl.emailExists(registerCustomerDto.getEmail())){
            return ResponseEntity.badRequest().body("Email already in use");
        }

        ResponseCustomerDTO savedCustomer = customerServiceImpl.register(registerCustomerDto);
        if(savedCustomer != null){
            return ResponseEntity.ok().body("Customer successfully registered!");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/get")
    public ResponseEntity<ResponseCustomerDTO> geById(@RequestParam Long id){
        ResponseCustomerDTO retrievedCustomer = customerServiceImpl.getById(id);
        if( retrievedCustomer != null ){
            return ResponseEntity.ok(retrievedCustomer);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ResponseCustomerDTO>> getAll(){
        List<ResponseCustomerDTO> customersList = customerServiceImpl.getAll();
        return ResponseEntity.ok(customersList);
    }

    @DeleteMapping("/delete")
    public void deleteById(@RequestParam Long customerId){
        customerServiceImpl.deleteById(customerId);
    }

}

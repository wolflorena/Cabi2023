package com.example.server.controller;

import com.example.server.repository.DTOs.Customers.CustomerEditDetailsDTO;
import com.example.server.repository.DTOs.Customers.CustomerPageDTO;
import com.example.server.repository.DTOs.Customers.RegisterCustomerDTO;
import com.example.server.repository.DTOs.Customers.ResponseCustomerDTO;
import com.example.server.repository.entity.Customer;
import com.example.server.service.implementation.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerServiceImpl customerServiceImpl;

    @Autowired
    public CustomerController(CustomerServiceImpl customerServiceImpl) {
        this.customerServiceImpl = customerServiceImpl;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterCustomerDTO registerCustomerDto){
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

    @GetMapping("/allPage")
    public ResponseEntity<CustomerPageDTO> getPatientsForAdmin(@RequestParam(required = true) int pageSize,
                                                               @RequestParam(required = true) int pageNumber) {
        return new ResponseEntity<>(
                customerServiceImpl.getAllCustomersForAdmin(
                        PageRequest.of(
                                pageNumber,
                                pageSize
                        )
                ),
                HttpStatus.OK
        );
    }

    @PutMapping("/accountStatus")
    public ResponseCustomerDTO updateAccountStatus(@RequestParam Long customerId, @RequestParam Customer.AccountStatus status) {
        return customerServiceImpl.editAccountStatus(customerId, status);
    }

    @PutMapping("/edit")
    public ResponseEntity<CustomerEditDetailsDTO> editCustomerDetails(@RequestParam Long customerId, @RequestBody CustomerEditDetailsDTO customer) {
        return new ResponseEntity<>(customerServiceImpl.editCustomerDetails(customerId, customer),HttpStatus.OK);
    }

    @PostMapping("/upload-avatar")
    public ResponseEntity<String> uploadAvatar(@RequestParam Long customerId, @RequestBody MultipartFile avatar){
        customerServiceImpl.uploadAvatar(customerId, avatar);
        return new ResponseEntity<>("Avatar uploaded successfully",HttpStatus.OK);
    }

    @GetMapping("/avatar")
    public ResponseEntity<byte[]> getAvatar(@RequestParam Long customerId) {
        byte[] avatar = customerServiceImpl.getAvatar(customerId);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(avatar);
    }

}

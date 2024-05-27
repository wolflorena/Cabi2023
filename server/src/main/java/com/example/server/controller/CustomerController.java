package com.example.server.controller;

import com.example.server.exception.types.CanNotDeactivateException;
import com.example.server.exception.types.CanNotDeleteException;
import com.example.server.repository.DTOs.Customers.*;
import com.example.server.repository.entity.Customer;
import com.example.server.service.implementation.CustomerServiceImpl;
import org.apache.coyote.Response;
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

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam Long customerId, @Valid @RequestBody ChangePasswordBodyDTO changePasswordBody){
        try{
            customerServiceImpl.changePassword(customerId, changePasswordBody.getCurrentPassword(), changePasswordBody.getNewPassword());
            return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<String> deleteAccount(@RequestParam Long customerId){
        try{
            customerServiceImpl.deleteAccount(customerId);
            return new ResponseEntity<>("Account deletion successful", HttpStatus.OK);
        } catch (Exception e){
            throw new CanNotDeleteException(e.getMessage());
        }
    }

    @PutMapping("/deactivate-account")
    public ResponseEntity<String> deactivateAccount(@RequestParam Long customerId){
        try{
            customerServiceImpl.deactivateAccount(customerId);
            return new ResponseEntity<>("Account deactivation successful", HttpStatus.OK);
        } catch (Exception e){
            throw new CanNotDeactivateException(e.getMessage());
        }
    }

}

package com.example.server.repository.DTOs.Customers;

import com.example.server.repository.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEditDetailsDTO {
    public String firstName;
    public String lastName;
    public String email;
    public String phoneNo;
    public LocalDate dateOfBirth;
    public String occupation;

    public CustomerEditDetailsDTO(Customer customer){
        this.firstName = customer.getFirstName();
        this.lastName = customer.getLastName();
        this.email = customer.getEmail();
        this.phoneNo = customer.getPhoneNo();
        this.dateOfBirth = customer.getDateOfBirth();
        this.occupation = customer.getOccupation();
    }
}

package com.example.server.repository.DTOs.Customers;

import com.example.server.repository.entity.Appointment;
import com.example.server.repository.entity.Customer.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
public class ResponseCustomerDTO {
    public Long customerId;
    public String firstName;
    public String lastName;
    public String email;
    public String phoneNo;
    public String occupation;
    public LocalDate dateOfBirth;
    @Lob
    public byte[] avatar;
    public AccountStatus accountStatus;
    public Set<Appointment> appointments;
}

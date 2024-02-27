package com.example.server.repository.entity;

import com.example.server.repository.DTOs.RegisterCustomerDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@NoArgsConstructor
@Data
public class Customer extends User {

    public enum AccountStatus {
        ACTIVE, INACTIVE, SUSPENDED
    }

    private String phoneNo;
    private String occupation;
    //LocalDate from Java 8 only has the date not the time too.
    private LocalDate dateOfBirth;
    @Lob
    private byte[] avatar;

    //@Enumerated(EnumType.STRING) is best used for storing the enum values as strings and not as numbers.
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @OneToMany(mappedBy = "customer")
    private Set<Appointment> appointments;

    @OneToMany(mappedBy = "customer")
    private Set<FormSignedByCustomer> formsSigned;

    @OneToMany(mappedBy = "customer")
    private Set<FormSeenByCustomer> formsSeen;

    public Customer(RegisterCustomerDTO registerCustomerDTO){
        super.setFirstName(registerCustomerDTO.getFirstName());
        super.setLastName(registerCustomerDTO.getLastName());
        super.setEmail(registerCustomerDTO.getEmail());
        super.setPassword(registerCustomerDTO.getPassword());
        this.phoneNo = registerCustomerDTO.getPhoneNo();
        this.dateOfBirth = registerCustomerDTO.getDateOfBirth();
        this.accountStatus = AccountStatus.ACTIVE;
        this.occupation = registerCustomerDTO.getOccupation();
    }

}


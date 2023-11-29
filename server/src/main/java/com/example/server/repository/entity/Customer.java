package com.example.server.repository.entity;

import com.example.server.repository.DTOs.RegisterCustomerDto;
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

    public Customer(RegisterCustomerDto registerCustomerDto){
        super.setFirstName(registerCustomerDto.getFirstName());
        super.setLastName(registerCustomerDto.getLastName());
        super.setEmail(registerCustomerDto.getEmail());
        super.setPassword(registerCustomerDto.getPassword());
        this.phoneNo = registerCustomerDto.getPhoneNo();
        this.dateOfBirth = registerCustomerDto.getDateOfBirth();
        this.accountStatus = AccountStatus.ACTIVE;
        this.occupation = registerCustomerDto.getOccupation();
    }

}


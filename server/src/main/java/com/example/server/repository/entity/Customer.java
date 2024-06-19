package com.example.server.repository.entity;

import com.example.server.repository.DTOs.Customers.RegisterCustomerDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Customer extends User {

    public enum AccountStatus {
        ACTIVE, INACTIVE, SUSPENDED
    }

    private String phoneNo;
    private String occupation;
    private LocalDate dateOfBirth;
    @Lob
    private byte[] avatar;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "customer-appointments")
    private Set<Appointment> appointments = new HashSet<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "customer-formEvents")
    private Set<FormEvent> formEvents = new HashSet<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "customer-pdfDocuments")
    private Set<PdfDocument> pdfDocumentList;


    public Customer(RegisterCustomerDTO registerCustomerDTO) {
        super.setFirstName(registerCustomerDTO.getFirstName());
        super.setLastName(registerCustomerDTO.getLastName());
        super.setEmail(registerCustomerDTO.getEmail());
        super.setPassword(registerCustomerDTO.getPassword());
        this.phoneNo = registerCustomerDTO.getPhoneNo();
        this.dateOfBirth = registerCustomerDTO.getDateOfBirth();
        this.accountStatus = AccountStatus.ACTIVE;
    }
}

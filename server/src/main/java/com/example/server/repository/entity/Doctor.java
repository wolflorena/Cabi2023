package com.example.server.repository.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Doctor extends User{

    private String address;
    private String phoneNo;
    @Lob
    private byte[] avatar;
    private LocalDate dateOfEmployment;
    @OneToMany(mappedBy = "doctor")
    private Set<Appointment> appointments;
    @OneToMany(mappedBy = "doctor")
    private Set<DoctorUnavailability> unavailabilities;
}
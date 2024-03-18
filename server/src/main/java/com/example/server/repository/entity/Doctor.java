package com.example.server.repository.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Doctor extends User{

    private String address;
    private String phoneNo;
    @Lob
    private byte[] avatar;
    private LocalDate dateOfEmployment;
    @OneToMany(mappedBy = "doctor")
    @JsonManagedReference(value="doctor-appointments")
    private Set<Appointment> appointments;
    @OneToMany(mappedBy = "doctor")
    @JsonManagedReference(value="doctor-unavailabilities")
    private Set<DoctorUnavailability> unavailabilities;
}
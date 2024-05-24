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
    @Column(name = "is_first_login", nullable = false, columnDefinition = "BIT DEFAULT 0")
    private boolean isFirstLogin;
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value="doctor-appointments")
    private Set<Appointment> appointments;
    @OneToMany(mappedBy = "doctor",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value="doctor-unavailabilities")
    private Set<DoctorUnavailability> unavailabilities;
}
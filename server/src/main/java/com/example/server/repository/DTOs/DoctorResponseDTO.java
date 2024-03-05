package com.example.server.repository.DTOs;

import com.example.server.repository.entity.Appointment;
import com.example.server.repository.entity.DoctorUnavailability;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
public class DoctorResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNo;
    @Lob
    private byte[] avatar;
    private String address;
    private LocalDate dateOfEmployment;
    private Set<Appointment> appointments;
    private Set<DoctorUnavailability> unavailabilities;
}

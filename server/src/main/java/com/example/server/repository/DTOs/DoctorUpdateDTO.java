package com.example.server.repository.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class DoctorUpdateDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phoneNo;
    private LocalDate dateOfEmployment;
}

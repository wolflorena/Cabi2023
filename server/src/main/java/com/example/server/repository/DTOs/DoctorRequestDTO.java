package com.example.server.repository.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class DoctorRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
    private String phoneNo;
    @Lob
    private byte[] avatar;
    private LocalDate dateOfEmployment;
}

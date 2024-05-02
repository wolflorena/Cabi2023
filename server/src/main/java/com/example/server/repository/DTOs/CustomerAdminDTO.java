package com.example.server.repository.DTOs;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerAdminDTO {
    private Long customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNo;
}

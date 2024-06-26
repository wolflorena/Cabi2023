package com.example.server.repository.DTOs.Authentication;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthenticationRequestDTO {
    private String email;
    private String password;
}

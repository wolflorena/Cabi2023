package com.example.server.repository.DTOs.Authentication;

import lombok.Data;

@Data
public class AuthenticationResponseDTO {
    private String jwtToken;

    // Constructor
    public AuthenticationResponseDTO(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}

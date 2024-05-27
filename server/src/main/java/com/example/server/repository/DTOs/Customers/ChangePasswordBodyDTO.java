package com.example.server.repository.DTOs.Customers;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class ChangePasswordBodyDTO {
    private String currentPassword;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "Password must contain at least one digit, one lowercase, one uppercase, one special character, and must be at least 8 characters long without spaces")
    private String newPassword;
}

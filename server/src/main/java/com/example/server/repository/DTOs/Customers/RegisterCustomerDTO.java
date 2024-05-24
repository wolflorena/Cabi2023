package com.example.server.repository.DTOs.Customers;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class RegisterCustomerDTO {

    @NotBlank(message = "First name cannot be empty")
    @Pattern(regexp = "^[A-Za-z]+$", message = "First name must contain only letters")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Last name must contain only letters")
    private String lastName;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be a valid email address")
    private String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "Password must contain at least one digit, one lowercase, one uppercase, one special character, and must be at least 8 characters long without spaces")
    private String password;

    @NotBlank(message = "Phone Number cannot be empty")
    private String phoneNo;

    @NotNull(message = "Date of birth cannot be null")
    private LocalDate dateOfBirth;
}

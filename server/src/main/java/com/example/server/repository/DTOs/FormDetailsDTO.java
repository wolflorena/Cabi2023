package com.example.server.repository.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class FormDetailsDTO {
    private String customerFirstName;
    private String customerLastName;
    private LocalDate timestamp;
}

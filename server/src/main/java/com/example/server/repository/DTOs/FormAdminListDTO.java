package com.example.server.repository.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class FormAdminListDTO {
    private Long formId;
    private String title;
    private LocalDate editedDate;
    private LocalTime editedTime;
}

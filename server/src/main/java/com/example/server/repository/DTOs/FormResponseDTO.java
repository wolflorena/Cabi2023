package com.example.server.repository.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class FormResponseDTO {
    private Long formId;
    private String title;
    private String description;
    private Boolean visibility;
    private LocalDate editedDate;
    private LocalTime editedTime;
}

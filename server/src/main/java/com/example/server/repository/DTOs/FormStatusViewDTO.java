package com.example.server.repository.DTOs;

import com.example.server.repository.entity.FormEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class FormStatusViewDTO {
    public String title;
    public FormEvent.FormEventType formEventType;
}

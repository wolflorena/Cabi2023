package com.example.server.repository.DTOs;

import com.example.server.repository.entity.FormEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class FormStatusViewDTO {
    public Long formId;
    public String title;

    public String description;

    public FormEvent.FormEventType formEventType;
}

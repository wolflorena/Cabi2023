package com.example.server.service;

import com.example.server.repository.DTOs.FormDetailsDTO;
import com.example.server.repository.DTOs.FormStatusViewDTO;
import com.example.server.repository.entity.Customer;
import com.example.server.repository.entity.FormEvent;

import java.util.List;

public interface FormEventService {
    void recordEvent(Long formId, Long customerId, FormEvent.FormEventType formEventType);

    List<FormDetailsDTO> getCustomersByFormEvent(Long formId, FormEvent.FormEventType formEventType);

    List<FormStatusViewDTO> getFormsWithStatus(Long customerId);
}

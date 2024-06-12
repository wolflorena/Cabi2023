package com.example.server.service.implementation;

import com.example.server.exception.types.FormEventAlreadyExistsException;
import com.example.server.exception.types.NotFoundException;
import com.example.server.repository.CustomerRepository;
import com.example.server.repository.DTOs.FormDetailsDTO;
import com.example.server.repository.DTOs.FormStatusViewDTO;
import com.example.server.repository.FormEventRepository;
import com.example.server.repository.FormRepository;
import com.example.server.repository.entity.Customer;
import com.example.server.repository.entity.Form;
import com.example.server.repository.entity.FormEvent;
import com.example.server.service.FormEventService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FormEventServiceImpl implements FormEventService {
    private final FormEventRepository formEventRepository;
    private final FormRepository formRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    public FormEventServiceImpl(FormEventRepository formEventRepository, FormRepository formRepository, CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.formEventRepository = formEventRepository;
        this.formRepository = formRepository;
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void recordEvent(Long formId, Long customerId, FormEvent.FormEventType formEventType) {
        Form form = formRepository.findById(formId).orElseThrow(()-> new NotFoundException("Form not found"));
        Customer customer = customerRepository.findById(customerId).orElseThrow(()-> new NotFoundException("Patient not found"));

        Optional<FormEvent> existingEvent = formEventRepository.findByFormIdAndCustomerIdAndFormEventType(formId, customerId, formEventType);

        if(existingEvent.isPresent()) {
            throw new FormEventAlreadyExistsException("The form has been already " + formEventType);
        }

        FormEvent formEvent = formEventRepository.findByFormIdAndCustomerId(form.getFormId(), customerId);
        formEvent.setFormEventType(formEventType);
        formEvent.setCustomer(customer);
        formEvent.setForm(form);
        formEvent.setTimestamp(LocalDate.now());

        formEventRepository.save(formEvent);
    }

    @Override
    public List<FormDetailsDTO> getCustomersByFormEvent(Long formId, FormEvent.FormEventType formEventType) {
        return formEventRepository.findByFormIdAndFormEventType(formId, formEventType).stream()
                .map(formEvent -> modelMapper.map(formEvent, FormDetailsDTO.class))
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<FormStatusViewDTO> getFormsWithStatus(Long customerId){
        List<FormEvent> formEvents = formEventRepository.findByCustomerId(customerId);

        return formEvents.stream()
                .map(event-> new FormStatusViewDTO(event.getForm().getTitle(), event.getFormEventType()))
                .toList();
    }
}

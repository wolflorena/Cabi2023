package com.example.server.service.implementation;

import com.example.server.exception.types.NotFoundException;
import com.example.server.repository.CustomerRepository;
import com.example.server.repository.DTOs.FormAdminListDTO;
import com.example.server.repository.DTOs.FormRequestDTO;
import com.example.server.repository.DTOs.FormResponseDTO;
import com.example.server.repository.DTOs.InventoryUpdateDTO;
import com.example.server.repository.FormEventRepository;
import com.example.server.repository.FormRepository;
import com.example.server.repository.entity.Customer;
import com.example.server.repository.entity.Form;
import com.example.server.repository.entity.FormEvent;
import com.example.server.repository.entity.Inventory;
import com.example.server.service.FormService;
import net.bytebuddy.asm.Advice;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FormServiceImpl implements FormService {
    private final FormRepository formRepository;
    private final FormEventRepository formEventRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    public FormServiceImpl(FormRepository formRepository, FormEventRepository formEventRepository, ModelMapper modelMapper, CustomerRepository customerRepository) {
        this.formRepository = formRepository;
        this.formEventRepository = formEventRepository;
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<FormAdminListDTO> getForms() {
        List<Form> forms = formRepository.findAll();
        return forms.stream()
                .map(form -> modelMapper.map(form, FormAdminListDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Form createForm(FormRequestDTO formRequestDTO) {
        Form form = new Form();

        form.setTitle(formRequestDTO.getTitle());
        form.setDescription(formRequestDTO.getDescription());
        form.setVisibility(formRequestDTO.getVisibility());
        form.setEditedTime(LocalTime.now());
        form.setEditedDate(LocalDate.now());
        formRepository.save(form);

        List<Customer> allCustomers = customerRepository.findAll();
        allCustomers.forEach(customer -> {
            FormEvent fe = new FormEvent();
            fe.setForm(form);
            fe.setCustomer(customer);
            fe.setFormEventType(FormEvent.FormEventType.NEW);
            fe.setTimestamp(LocalDate.now());
            formEventRepository.save(fe);
        });
        return form;
    }

    @Override
    public FormResponseDTO getFormById(Long formId) {
        Form form =  formRepository.findById(formId).orElseThrow(()-> new NotFoundException("Form not found"));
        return modelMapper.map(form, FormResponseDTO.class);
    }

    @Transactional
    @Override
    public boolean deleteForm(Long formId) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new NotFoundException("Form not found"));

        formEventRepository.deleteAll(form.getFormEvents());
        formRepository.delete(form);
        return true;
    }

    @Override
    public FormResponseDTO updateForm(Long formId, FormRequestDTO formRequestDTO) {
        Form form = formRepository.findById(formId).orElseThrow(()-> new NotFoundException("Form not found"));

        if (formRequestDTO.getTitle() != null) {
                form.setTitle(formRequestDTO.getTitle());
        }

        if (formRequestDTO.getDescription() != null) {
            form.setDescription(formRequestDTO.getDescription());
        }

        if (formRequestDTO.getVisibility() != null) {
            form.setVisibility(formRequestDTO.getVisibility());
        }

        form.setEditedDate(LocalDate.now());
        form.setEditedTime(LocalTime.now());

        formRepository.save(form);
        return modelMapper.map(form, FormResponseDTO.class);
    }

    @Override
    public List<FormResponseDTO> getAllFormsForCustomer(){
        List<Form> forms = formRepository.findAll();
        return forms.stream().filter(Form::getVisibility).map(form-> modelMapper.map(form, FormResponseDTO.class)).toList();
    }
}

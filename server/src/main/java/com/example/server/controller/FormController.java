package com.example.server.controller;

import com.example.server.repository.DTOs.FormAdminListDTO;
import com.example.server.repository.DTOs.FormDetailsDTO;
import com.example.server.repository.DTOs.FormRequestDTO;
import com.example.server.repository.DTOs.FormResponseDTO;
import com.example.server.repository.entity.Customer;
import com.example.server.repository.entity.Form;
import com.example.server.repository.entity.FormEvent;
import com.example.server.service.FormEventService;
import com.example.server.service.FormService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/form")
public class FormController {
    private final FormService formService;
    private final FormEventService formEventService;

    public FormController(FormService formService, FormEventService formEventService) {
        this.formService = formService;
        this.formEventService = formEventService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<FormAdminListDTO>> getAllForms() {
        return new ResponseEntity<>(
                formService.getForms(),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<Form> addForm(@RequestBody FormRequestDTO formRequestDTO) {
        return new ResponseEntity<>(formService.createForm(formRequestDTO), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<FormResponseDTO> getForm(@RequestParam Long formId) {
        return new ResponseEntity<>(formService.getFormById(formId), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteForm(@RequestParam Long formId) {
        return new ResponseEntity<>(formService.deleteForm(formId), HttpStatus.OK);
    }

    @PostMapping("/{formId}/view")
    public String viewForm(@PathVariable Long formId, @RequestParam Long customerId) {
        formEventService.recordEvent(formId, customerId, FormEvent.FormEventType.VIEWED);
        return "Form viewed";
    }

    @PostMapping("/{formId}/sign")
    public String signForm(@PathVariable Long formId, @RequestParam Long customerId) {
        formEventService.recordEvent(formId, customerId, FormEvent.FormEventType.SIGNED);
        return "Form signed";
    }

    @GetMapping("/{formId}/customers")
    public Map<String, List<FormDetailsDTO>> getCustomersByFormEvents(@PathVariable Long formId) {
        List<FormDetailsDTO> viewedCustomers = formEventService.getCustomersByFormEvent(formId, FormEvent.FormEventType.VIEWED);
        List<FormDetailsDTO> signedCustomers = formEventService.getCustomersByFormEvent(formId, FormEvent.FormEventType.SIGNED);
        Map<String, List<FormDetailsDTO>> result = new HashMap<>();
        result.put("viewed", viewedCustomers);
        result.put("signed", signedCustomers);
        return result;
    }

    @PatchMapping("/update-form")
    public FormResponseDTO updateForm(@RequestParam Long formId, @RequestBody FormRequestDTO formRequestDTO) {
        return formService.updateForm(formId, formRequestDTO);
    }
}

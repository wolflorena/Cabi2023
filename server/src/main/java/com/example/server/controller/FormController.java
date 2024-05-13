package com.example.server.controller;

import com.example.server.repository.DTOs.FormAdminListDTO;
import com.example.server.repository.DTOs.FormRequestDTO;
import com.example.server.repository.entity.Form;
import com.example.server.service.FormService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/form")
public class FormController {
    private final FormService formService;

    public FormController(FormService formService) {
        this.formService = formService;
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
    public ResponseEntity<Form> getForm(@RequestParam Long formId) {
        return new ResponseEntity<>(formService.getFormById(formId), HttpStatus.OK);
    }
}

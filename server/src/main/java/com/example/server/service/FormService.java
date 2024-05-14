package com.example.server.service;

import com.example.server.repository.DTOs.FormAdminListDTO;
import com.example.server.repository.DTOs.FormRequestDTO;
import com.example.server.repository.DTOs.FormResponseDTO;
import com.example.server.repository.entity.Form;

import java.util.List;

public interface FormService {

    List<FormAdminListDTO> getForms();
    Form createForm(FormRequestDTO formRequestDTO);
    FormResponseDTO getFormById(Long formId);
    boolean deleteForm(Long formId);
    FormResponseDTO updateForm(Long formId, FormRequestDTO formRequestDTO);
}

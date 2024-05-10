package com.example.server.service;

import com.example.server.repository.DTOs.FormAdminListDTO;
import com.example.server.repository.DTOs.FormRequestDTO;
import com.example.server.repository.entity.Form;

import java.util.List;

public interface FormService {

    List<FormAdminListDTO> getForms();
    Form createForm(FormRequestDTO formRequestDTO);

}

package com.example.server.controller;

import com.example.server.repository.DTOs.DoctorRequestDTO;
import com.example.server.service.implementation.DoctorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    @Autowired
    private DoctorServiceImpl doctorServiceImpl;

    public DoctorController(DoctorServiceImpl doctorServiceImpl){
        this.doctorServiceImpl = doctorServiceImpl;
    }

    @PostMapping("/addDoctor")
    public ResponseEntity<Object> addDoctor(@RequestBody DoctorRequestDTO doctorRequestDTO){
        return new ResponseEntity<>(doctorServiceImpl.addDoctor(doctorRequestDTO), HttpStatus.OK);
    }
}

package com.example.server.controller;

import com.example.server.repository.DTOs.DoctorRequestDTO;
import com.example.server.repository.DTOs.DoctorResponseDTO;
import com.example.server.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    public DoctorController(DoctorService doctorService){
        this.doctorService = doctorService;
    }

    @PostMapping("/addDoctor")
    public ResponseEntity<Object> addDoctor(@RequestBody DoctorRequestDTO doctorRequestDTO){
        return new ResponseEntity<>(doctorService.addDoctor(doctorRequestDTO), HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getDoctorById(@RequestParam Long doctorId) {
        DoctorResponseDTO doctor = doctorService.getDoctorById(doctorId);
        return ResponseEntity.ok(doctor);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<DoctorResponseDTO>> getDoctors() {
        List<DoctorResponseDTO> doctors = doctorService.getAll();
        return ResponseEntity.ok(doctors);
    }
}

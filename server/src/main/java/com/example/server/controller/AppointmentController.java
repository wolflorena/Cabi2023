package com.example.server.controller;

import com.example.server.repository.DTOs.AppointmentRequestDTO;
import com.example.server.repository.DTOs.AppointmentResponseDTO;
import com.example.server.service.AppointmentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/create")
    public ResponseEntity<AppointmentResponseDTO> addAppointment(@RequestBody AppointmentRequestDTO appointmentRequestDTO, @RequestParam Long customerId){
        return new ResponseEntity<>(appointmentService.addAppointment(appointmentRequestDTO, customerId), HttpStatus.OK);
    }

    @GetMapping
    public AppointmentResponseDTO getAppointmentById(@RequestParam(name="appointmentId") Long appointmentId){
        return appointmentService.getAppointmentById(appointmentId);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointments() {
        List<AppointmentResponseDTO> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/getAllByDateAndDoctor")
    public ResponseEntity<List<AppointmentResponseDTO>> getApoointmentsByDateAndDoctor(@RequestParam(name="date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam(name="doctorId")Long doctorId){
        List<AppointmentResponseDTO> appointments = appointmentService.getAllAppointmentsByDateAndDoctor(date, doctorId);
        return ResponseEntity.ok(appointments);
    }
}

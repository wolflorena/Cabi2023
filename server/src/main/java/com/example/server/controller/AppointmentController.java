package com.example.server.controller;

import com.example.server.repository.DTOs.*;
import com.example.server.repository.entity.Appointment;
import com.example.server.service.AppointmentService;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/create")
    public ResponseEntity<AppointmentResponseDTO> addAppointment(@RequestBody AppointmentRequestDTO appointmentRequestDTO) {
        return new ResponseEntity<>(appointmentService.addAppointment(appointmentRequestDTO), HttpStatus.OK);
    }

    @GetMapping
    public AppointmentDetailDTO getAppointmentById(@RequestParam(name="appointmentId") Long appointmentId) {
        return appointmentService.getAppointmentById(appointmentId);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteAppointment(@RequestParam(name="appointmentId") Long appointmentId) {
        return new ResponseEntity<>(appointmentService.deleteAppointment(appointmentId), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointments() {
        List<AppointmentResponseDTO> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/getAllForCalendar")
    public ResponseEntity<List<AppointmentCalendarDTO>> getAppointmentsForCalendar() {
        List<AppointmentCalendarDTO> appointments = appointmentService.getAllAppointmentsForCalendar();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/allPage")
    public ResponseEntity<AppointmentPageDTO> getAppointmentsForAdmin(@RequestParam(required = true) int pageSize,
                                                                      @RequestParam(required = true) int pageNumber,
                                                                      @RequestParam(required = false) List<Long> doctorIds,
                                                                      @RequestParam(required = false)Appointment.AppointmentStatus status) {
        return new ResponseEntity<>(
                appointmentService.getAllAppointmentsForAdmin(
                        PageRequest.of(
                                pageNumber,
                                pageSize
                        ),
                        doctorIds,
                        status
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/getAllByDateAndDoctor")
    public ResponseEntity<List<AppointmentResponseDTO>> getApoointmentsByDateAndDoctor(@RequestParam(name="date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                                                       @RequestParam(name="doctorId")Long doctorId){
        List<AppointmentResponseDTO> appointments = appointmentService.getAllAppointmentsByDateAndDoctor(date, doctorId);
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/status")
    public AppointmentResponseDTO updateAppointmentStatus(@RequestParam Long appointmentId,
                                                          @RequestParam Appointment.AppointmentStatus status) {
        return appointmentService.updateAppointmentStatus(appointmentId, status);
    }

    @GetMapping("/nextByDoctor")
    public ResponseEntity<List<AppointmentDoctorDashboardDTO>> getNextApoointmentsByDoctor(@RequestParam(name="doctorId")Long doctorId){
        List<AppointmentDoctorDashboardDTO> appointments = appointmentService.getNextAppointments(doctorId);
        return ResponseEntity.ok(appointments);
    }
}

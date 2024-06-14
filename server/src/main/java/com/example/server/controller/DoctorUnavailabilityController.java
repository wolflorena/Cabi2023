package com.example.server.controller;

import com.example.server.repository.DTOs.VacationRequestDTO;
import com.example.server.repository.entity.DoctorUnavailability;
import com.example.server.service.DoctorUnavailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Year;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/unavailability")
public class DoctorUnavailabilityController {
    @Autowired
    private DoctorUnavailabilityService doctorUnavailabilityService;

    public DoctorUnavailabilityController(DoctorUnavailabilityService doctorUnavailabilityService) {
        this.doctorUnavailabilityService = doctorUnavailabilityService;
    }

    @GetMapping("/{doctorId}")
    public List<DoctorUnavailability> getUnavailabilitiesByDoctorId(@PathVariable Long doctorId) {
        return doctorUnavailabilityService.getUnavailabilitiesByDoctorId(doctorId);
    }

    @GetMapping("/forCalendar")
    public List<VacationRequestDTO> getVacationsForCalendar() {
        return doctorUnavailabilityService.getAllVacationsForCalendar();
    }

    @GetMapping("/forCalendarForYear")
    public List<VacationRequestDTO> getVacationsForCalendarForYearForDoctor(@RequestParam Long doctorId) {
        Year selectedYear = Year.of(LocalDate.now().getYear());
        return doctorUnavailabilityService.getAllVacationsForCalendarForYear(selectedYear, doctorId);
    }

    @PostMapping
    public VacationRequestDTO createUnavailability(@RequestBody VacationRequestDTO unavailability) {
        return doctorUnavailabilityService.createUnavailability(unavailability);
    }

    @DeleteMapping("/{id}")
    public void deleteUnavailability(@PathVariable Long id) {
        doctorUnavailabilityService.deleteUnavailability(id);
    }
}

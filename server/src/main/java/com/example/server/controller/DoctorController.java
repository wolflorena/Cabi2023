package com.example.server.controller;

import com.example.server.repository.DTOs.*;
import com.example.server.repository.DTOs.Customers.ChangePasswordBodyDTO;
import com.example.server.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDate;

import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Object> addDoctor(@RequestBody DoctorAdminRequestDTO doctorRequestDTO){
        return new ResponseEntity<>(doctorService.addDoctor(doctorRequestDTO), HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getDoctorById(@RequestParam Long doctorId) {
        DoctorResponseDTO doctor = doctorService.getDoctorById(doctorId);
        return ResponseEntity.ok(doctor);
    }

    @GetMapping("/getAvatar")
    public ResponseEntity<byte[]> getDoctorAvatarById(@RequestParam Long doctorId) {
        byte[] doctorAvatar = doctorService.getDoctorAvatarById(doctorId);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(doctorAvatar);
    }
    @PostMapping("/upload-avatar")
    public ResponseEntity<String> uploadAvatar(@RequestParam Long doctorId, @RequestBody MultipartFile avatar){
        doctorService.uploadAvatar(doctorId, avatar);
        return new ResponseEntity<>("Avatar uploaded successfully",HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<DoctorResponseDTO>> getDoctors() {
        List<DoctorResponseDTO> doctors = doctorService.getAll();
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/available-dates")
    public ResponseEntity<List<LocalDate>> getAvailableDates(@RequestParam Long doctorId,
                                                             @RequestParam Long serviceId) {
        List<LocalDate> availableDates = doctorService.findAvailableDates(doctorId, serviceId);
        return ResponseEntity.ok(availableDates);
    }

    @GetMapping("/available-hours")
    public ResponseEntity<List<LocalTime>> getAvailableHours(@RequestParam Long doctorId,
                                                             @RequestParam Long serviceId,
                                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<LocalTime> availableHours = doctorService.findAvailableHours(doctorId, serviceId, date);
        return ResponseEntity.ok(availableHours);
    }

    @PatchMapping("/update-doctor")
    public DoctorUpdateDTO updateDoctor(@RequestParam Long doctorId, @RequestBody DoctorUpdateDTO doctorUpdateDTO) {
        return doctorService.updateDoctor(doctorId, doctorUpdateDTO);
    }

    @GetMapping("/services/current-month")
    public List<TreatmentTypesDTO> getDoctorServicesInCurrentMonth(@RequestParam Long doctorId) {
        return doctorService.getDoctorServicesInCurrentMonth(doctorId);
    }

    @DeleteMapping
    public boolean deleteById(@RequestParam Long doctorId) {
        return doctorService.deleteDoctorById(doctorId);
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestParam Long doctorId, @RequestBody ChangePasswordBodyDTO changePasswordBody){
        return new ResponseEntity<>(doctorService.changePassword(doctorId, changePasswordBody.getCurrentPassword(), changePasswordBody.getNewPassword()), HttpStatus.OK );
    }
}

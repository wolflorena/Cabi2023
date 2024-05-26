package com.example.server.service.implementation;

import com.example.server.exception.types.NotFoundException;
import com.example.server.repository.DTOs.AppointmentResponseDTO;
import com.example.server.repository.DTOs.VacationRequestDTO;
import com.example.server.repository.DoctorRepository;
import com.example.server.repository.DoctorUnavailabilityRepository;
import com.example.server.repository.entity.Doctor;
import com.example.server.repository.entity.DoctorUnavailability;
import com.example.server.service.DoctorUnavailabilityService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorUnavailabilityServiceImpl implements DoctorUnavailabilityService {
    private DoctorUnavailabilityRepository doctorUnavailabilityRepository;
    private DoctorRepository doctorRepository;
    private ModelMapper modelMapper;

    public DoctorUnavailabilityServiceImpl(DoctorUnavailabilityRepository doctorUnavailabilityRepository,
                                           DoctorRepository doctorRepository,
                                           ModelMapper modelMapper) {
        this.doctorUnavailabilityRepository = doctorUnavailabilityRepository;
        this.doctorRepository = doctorRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<DoctorUnavailability> getUnavailabilitiesByDoctorId(Long doctorId) {
        return doctorUnavailabilityRepository.findByDoctorId(doctorId);
    }

    @Override
    public VacationRequestDTO createUnavailability(VacationRequestDTO unavailability) {
        DoctorUnavailability doctorUnavailability = new DoctorUnavailability();
        Doctor doctor = doctorRepository.findById(unavailability.getDoctorId()).orElseThrow(() -> new NotFoundException("Doctor not found"));

        if (unavailability.getStartDate() != unavailability.getStartDate()) {
            if (doctorUnavailabilityRepository.existsByStartDateAndEndDateAndDoctorId(
                    unavailability.getStartDate(),
                    unavailability.getEndDate(),
                    unavailability.getDoctorId())) {
                throw new RuntimeException("Vacation exists");
            } else {
                doctorUnavailability.setDoctor(doctor);
                doctorUnavailability.setStartDate(unavailability.getStartDate());
                doctorUnavailability.setEndDate(unavailability.getEndDate());
            }
        } else {
            if (unavailability.getStartTime() != unavailability.getEndTime()) {
                if (doctorUnavailabilityRepository.existsByStartTimeAndEndTimeAndStartDateAndDoctorId(
                        unavailability.getStartTime(),
                        unavailability.getEndTime(),
                        unavailability.getStartDate(),
                        unavailability.getDoctorId()
                )) {
                    throw new RuntimeException("Vacation exists");
                } else {
                    doctorUnavailability.setDoctor(doctor);
                    doctorUnavailability.setStartTime(unavailability.getStartTime());
                    doctorUnavailability.setEndTime(unavailability.getEndTime());
                    doctorUnavailability.setStartDate(unavailability.getStartDate());
                    doctorUnavailability.setEndDate(unavailability.getEndDate());
                }
            } else {
                doctorUnavailability.setDoctor(doctor);
                doctorUnavailability.setStartDate(unavailability.getStartDate());
                doctorUnavailability.setEndDate(unavailability.getEndDate());
            }
        }

        doctor.getUnavailabilities().add(doctorUnavailability);
        return modelMapper.map(doctorUnavailabilityRepository.save(doctorUnavailability), VacationRequestDTO.class);
    }

    @Override
    public void deleteUnavailability(Long id) {
        doctorUnavailabilityRepository.deleteById(id);
    }

}

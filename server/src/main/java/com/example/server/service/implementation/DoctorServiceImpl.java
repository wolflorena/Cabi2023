package com.example.server.service.implementation;

import com.example.server.repository.DTOs.DoctorRequestDTO;
import com.example.server.repository.DTOs.DoctorResponseDTO;
import com.example.server.repository.DoctorRepository;
import com.example.server.repository.entity.Doctor;
import com.example.server.service.DoctorService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;

    public DoctorServiceImpl(DoctorRepository doctorRepository, ModelMapper modelMapper){
        this.doctorRepository = doctorRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public DoctorResponseDTO addDoctor(DoctorRequestDTO doctorRequestDTO){
        Doctor doctor = new Doctor();
        doctor.setFirstName(doctorRequestDTO.getFirstName());
        doctor.setLastName(doctorRequestDTO.getLastName());
        doctor.setEmail(doctorRequestDTO.getEmail());
        doctor.setPassword(doctorRequestDTO.getPassword());
        doctor.setPhoneNo(doctorRequestDTO.getPhoneNo());
        doctor.setAvatar(doctorRequestDTO.getAvatar());
        doctor.setAddress(doctorRequestDTO.getAddress());
        doctor.setDateOfEmployment(doctorRequestDTO.getDateOfEmployment());
        doctorRepository.save(doctor);
        return modelMapper.map(doctor, DoctorResponseDTO.class);
    }


}

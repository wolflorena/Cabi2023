package com.example.server.service.implementation;

import com.example.server.exception.types.AppointmentExistsException;
import com.example.server.repository.AppointmentRepository;
import com.example.server.repository.CustomerRepository;
import com.example.server.repository.DTOs.AppointmentRequestDTO;
import com.example.server.repository.DTOs.AppointmentResponseDTO;
import com.example.server.repository.DoctorRepository;
import com.example.server.repository.ServiceRepository;
import com.example.server.repository.entity.Appointment;
import com.example.server.repository.entity.Customer;
import com.example.server.repository.entity.Doctor;
import com.example.server.service.AppointmentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final DoctorRepository doctorRepository;
    private final ServiceRepository serviceRepository;
    private final ModelMapper modelMapper;

    private AppointmentServiceImpl(AppointmentRepository appointmentRepository, CustomerRepository customerRepository , DoctorRepository doctorRepository, ServiceRepository serviceRepository, ModelMapper modelMapper){
        this.appointmentRepository = appointmentRepository;
        this.customerRepository = customerRepository;
        this.doctorRepository = doctorRepository;
        this.serviceRepository = serviceRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public AppointmentResponseDTO addAppointment(AppointmentRequestDTO appointmentRequestDTO, Long customerId){
        if(appointmentRepository.existsByDateAndTimeAndDoctorId(
                appointmentRequestDTO.getDate(),
                appointmentRequestDTO.getTime(),
                appointmentRequestDTO.getDoctorId()
        )){
            throw new AppointmentExistsException("An appointment already exists at this date and time for the selected doctor.");
        }
        Appointment appointment = new Appointment();
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        Doctor doctor = doctorRepository.findById(appointmentRequestDTO.getDoctorId()).orElseThrow();
        com.example.server.repository.entity.Service service = serviceRepository.findById(appointmentRequestDTO.getServiceId()).orElseThrow();
        appointment.setCustomer(customer);
        appointment.setDoctor(doctor);
        appointment.setService(service);
        appointment.setDate(appointmentRequestDTO.getDate());
        appointment.setTime(appointmentRequestDTO.getTime());
        appointment.setFinalDuration(service.getDuration());
        appointment.setStatus(Appointment.AppointmentStatus.SCHEDULED);

        customer.getAppointments().add(appointment);
        return modelMapper.map(appointmentRepository.save(appointment), AppointmentResponseDTO.class);
    }

    @Override
    public AppointmentResponseDTO getAppointmentById(Long appointmentId){
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow();
        return modelMapper.map(appointment, AppointmentResponseDTO.class);
    }

    @Override
    public List<AppointmentResponseDTO> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .map(appointment -> modelMapper.map(appointment, AppointmentResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponseDTO> getAllAppointmentsByDateAndDoctor(LocalDate date, Long doctorId){
        List<Appointment> appointments = appointmentRepository.findByDateAndDoctorId(date,doctorId);
        return appointments.stream()
                .map(appointment -> modelMapper.map(appointment, AppointmentResponseDTO.class))
                .collect(Collectors.toList());
    }
}

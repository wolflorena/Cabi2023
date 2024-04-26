package com.example.server.service.implementation;

import com.example.server.exception.types.AppointmentExistsException;
import com.example.server.repository.AppointmentRepository;
import com.example.server.repository.CustomerRepository;
import com.example.server.repository.DTOs.*;
import com.example.server.repository.DoctorRepository;
import com.example.server.repository.ServiceRepository;
import com.example.server.repository.entity.Appointment;
import com.example.server.repository.entity.Customer;
import com.example.server.repository.entity.Doctor;
import com.example.server.service.AppointmentService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

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
        appointment.setStatus(Appointment.AppointmentStatus.REQUESTED);

        customer.getAppointments().add(appointment);
        return modelMapper.map(appointmentRepository.save(appointment), AppointmentResponseDTO.class);
    }

    @Override
    public AppointmentDetailDTO getAppointmentById(Long appointmentId){
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow();
        return modelMapper.map(appointment, AppointmentDetailDTO.class);
    }

    @Override
    public Boolean deleteAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new AppointmentExistsException("Appointment not found"));
        appointmentRepository.delete(appointment);
        return true;
    }

    @Override
    public List<AppointmentResponseDTO> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .map(appointment -> modelMapper.map(appointment, AppointmentResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentCalendarDTO> getAllAppointmentsForCalendar() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .map(appointment ->  modelMapper.map(appointment, AppointmentCalendarDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentPageDTO getAllAppointmentsForAdmin(Pageable pageable,
                                                         List<Long> doctorIds,
                                                         Appointment.AppointmentStatus status) {
        AppointmentPageDTO appointmentPageDTO = new AppointmentPageDTO();
        Page<Appointment> appointments;

        if(doctorIds == null || doctorIds.isEmpty()) {
            if(status == null) {
                appointments = appointmentRepository.findAll(pageable);
            } else {
                appointments = appointmentRepository.findAllByStatus(status, pageable);
            }
        } else {
            if(status == null) {
                appointments = appointmentRepository.findAllByDoctorIdIn(doctorIds, pageable);
            } else {
                appointments = appointmentRepository.findAllByDoctorIdInAndStatus(doctorIds, status, pageable);
            }
        }

        List<AppointmentAdminDTO> result = appointments.getContent()
                .stream()
                .map(appointment -> modelMapper.map(appointment, AppointmentAdminDTO.class))
                .collect(Collectors.toList());

        appointmentPageDTO.setTotal((int) appointments.getTotalElements());
        appointmentPageDTO.setPagedAppointments(new PageImpl<>(result, pageable, appointments.getTotalElements()));
        return appointmentPageDTO;
    }

    @Override
    public List<AppointmentResponseDTO> getAllAppointmentsByDateAndDoctor(LocalDate date, Long doctorId){
        List<Appointment> appointments = appointmentRepository.findByDateAndDoctorId(date,doctorId);
        return appointments.stream()
                .map(appointment -> modelMapper.map(appointment, AppointmentResponseDTO.class))
                .collect(Collectors.toList());
    }
}

package com.example.server.service.implementation;

import com.example.server.repository.AppointmentRepository;
import com.example.server.repository.DTOs.AppointmentTimeDTO;
import com.example.server.repository.DTOs.DoctorRequestDTO;
import com.example.server.repository.DTOs.DoctorResponseDTO;
import com.example.server.repository.DoctorRepository;
import com.example.server.repository.ServiceRepository;
import com.example.server.repository.entity.Appointment;
import com.example.server.repository.entity.Doctor;
import com.example.server.service.DoctorService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final ServiceRepository serviceRepository;
    private final ModelMapper modelMapper;

    public DoctorServiceImpl(DoctorRepository doctorRepository,
                             AppointmentRepository appointmentRepository,
                             ServiceRepository serviceRepository,
                             ModelMapper modelMapper){
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.serviceRepository = serviceRepository;
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

    @Override
    public DoctorResponseDTO getDoctorById(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();
        return modelMapper.map(doctor, DoctorResponseDTO.class);
    }

    @Override
    public List<DoctorResponseDTO> getAll() {
        List<Doctor> doctors = doctorRepository.findAll();
        return doctors.stream()
                .map(doctor -> modelMapper.map(doctor, DoctorResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<LocalDate> findAvailableDates(Long doctorId, Long serviceId) {
        LocalDate today = LocalDate.now();
        LocalDate end = today.plusMonths(1);
        List<LocalDate> availableDates = new ArrayList<>();
        com.example.server.repository.entity.Service service = serviceRepository.findById(serviceId).orElseThrow();

        for (LocalDate date = today; date.isBefore(end); date = date.plusDays(1)) {
            List<Appointment> bookedAppointments = appointmentRepository.findBookedTimesAndDurationsByDoctorIdAndDate(doctorId, date);
            if (isDayAvailable(bookedAppointments, service.getDuration())) {
                availableDates.add(date);
            }
        }
        return availableDates;
    }

    private boolean isDayAvailable(List<Appointment> bookedAppointments, int appointmentDuration) {
        LocalTime startOfWork = LocalTime.of(9, 0);
        LocalTime endOfWork = LocalTime.of(17, 0);
        LocalTime currentTime = startOfWork;

        for (Appointment appointment : bookedAppointments) {
            LocalTime appointmentStart = appointment.getTime();
            if (!currentTime.isBefore(appointmentStart)) {
                continue; // Ignoră programările care nu afectează intervalul de timp curent
            }

            int availableMinutes = (int) ChronoUnit.MINUTES.between(currentTime, appointmentStart);
            if (availableMinutes >= appointmentDuration) {
                return true; // Există un slot disponibil între două programări
            }

            // Muta timpul curent la sfârșitul acestei programări
            currentTime = appointmentStart.plusMinutes(appointment.getFinalDuration());
        }

        // Verifică disponibilitatea după ultima programare a zilei
        return ChronoUnit.MINUTES.between(currentTime, endOfWork) >= appointmentDuration;
    }

    public List<LocalTime> findAvailableHours(Long doctorId, Long serviceId, LocalDate date) {
        com.example.server.repository.entity.Service service = serviceRepository.findById(serviceId).orElseThrow();
        int serviceDuration = service.getDuration();

        List<Appointment> appointments = appointmentRepository.findBookedTimesAndDurationsByDoctorIdAndDate(doctorId, date);
        return calculateAvailableHours(appointments, serviceDuration);
    }

    private List<LocalTime> calculateAvailableHours1(List<Appointment> bookedAppointments, int duration) {
        List<LocalTime> availableHours = new ArrayList<>();
        LocalTime startOfWork = LocalTime.of(9, 0);
        LocalTime endOfWork = LocalTime.of(17, 0);
        LocalTime currentTime = startOfWork;

        if (bookedAppointments.isEmpty() && duration <= ChronoUnit.MINUTES.between(startOfWork, endOfWork)) {
            for (LocalTime time = startOfWork; time.plusMinutes(duration).isBefore(endOfWork.plusMinutes(1)); time = time.plusMinutes(duration)) {
                availableHours.add(time);
            }
            return availableHours;
        }

        for (Appointment appointment : bookedAppointments) {
            while (currentTime.plusMinutes(duration).isBefore(appointment.getTime()) && currentTime.plusMinutes(duration).isBefore(endOfWork.plusMinutes(1))) {
                availableHours.add(currentTime);
                currentTime = currentTime.plusMinutes(duration);
            }
            currentTime = appointment.getTime().plusMinutes(appointment.getFinalDuration());
        }

        while (currentTime.plusMinutes(duration).isBefore(endOfWork.plusMinutes(1))) {
            availableHours.add(currentTime);
            currentTime = currentTime.plusMinutes(duration);
        }

        return availableHours;
    }

    private List<LocalTime> calculateAvailableHours(List<Appointment> bookedAppointments, int duration) {
        List<LocalTime> availableHours = new ArrayList<>();
        LocalTime startOfWork = LocalTime.of(9, 0);
        LocalTime endOfWork = LocalTime.of(17, 0);
        LocalTime currentTime = startOfWork;

        // Dacă nu există programări, populează întreaga zi lucrătoare la fiecare 30 de minute
        if (bookedAppointments.isEmpty() && duration <= ChronoUnit.MINUTES.between(startOfWork, endOfWork)) {
            for (LocalTime time = startOfWork; time.plusMinutes(duration).isBefore(endOfWork.plusMinutes(1)); time = time.plusMinutes(30)) {
                availableHours.add(time);
            }
            return availableHours;
        }

        // Generarea sloturilor între programări existente la intervale de 30 de minute
        for (Appointment appointment : bookedAppointments) {
            while (currentTime.plusMinutes(duration).isBefore(appointment.getTime().plusMinutes(1)) && currentTime.plusMinutes(duration).isBefore(endOfWork.plusMinutes(1))) {
                availableHours.add(currentTime);
                currentTime = currentTime.plusMinutes(30); // Incrementăm la fiecare 30 de minute
            }
            // Muta timpul curent la sfârșitul acestei programări
            currentTime = appointment.getTime().plusMinutes(appointment.getFinalDuration());
        }

        // Verifică disponibilitatea după ultima programare până la sfârșitul programului de lucru
        while (currentTime.plusMinutes(duration).isBefore(endOfWork.plusMinutes(1))) {
            availableHours.add(currentTime);
            currentTime = currentTime.plusMinutes(30); // Continuăm să incrementăm la fiecare 30 de minute
        }

        return availableHours;
    }

}

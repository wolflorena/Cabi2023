package com.example.server.service.implementation;

import com.example.server.exception.types.EmailExistsException;
import com.example.server.repository.AppointmentRepository;
import com.example.server.repository.DTOs.*;
import com.example.server.repository.DoctorRepository;
import com.example.server.repository.ServiceRepository;
import com.example.server.repository.entity.Appointment;
import com.example.server.repository.entity.Doctor;
import com.example.server.service.DoctorService;
import com.example.server.service.SendEmailService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final ServiceRepository serviceRepository;
    private final SendEmailService sendEmailService;
    private final ModelMapper modelMapper;

    public DoctorServiceImpl(DoctorRepository doctorRepository,
                             AppointmentRepository appointmentRepository,
                             ServiceRepository serviceRepository,
                             SendEmailService sendEmailService,
                             ModelMapper modelMapper) {

        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.serviceRepository = serviceRepository;
        this.sendEmailService = sendEmailService;
        this.modelMapper = modelMapper;
    }

    @Override
    public DoctorResponseDTO addDoctor(DoctorAdminRequestDTO doctorAdminRequestDTO) {
        Doctor doctor = new Doctor();
        if(doctorRepository.findByEmail(doctorAdminRequestDTO.getEmail()).isEmpty()) {
            doctor.setFirstName(doctorAdminRequestDTO.getFirstName());
            doctor.setLastName(doctorAdminRequestDTO.getLastName());
            doctor.setEmail(doctorAdminRequestDTO.getEmail());
            doctor.setFirstLogin(true);

            doctorRepository.save(doctor);
            sendEmailService.sendPasswordToDoctor(doctor.getId());
            return modelMapper.map(doctor, DoctorResponseDTO.class);
        } else {
            throw new EmailExistsException("Email already exists");
        }
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
                continue;
            }

            int availableMinutes = (int) ChronoUnit.MINUTES.between(currentTime, appointmentStart);
            if (availableMinutes >= appointmentDuration) {
                return true;
            }

            currentTime = appointmentStart.plusMinutes(appointment.getFinalDuration());
        }

        return ChronoUnit.MINUTES.between(currentTime, endOfWork) >= appointmentDuration;
    }

    public List<LocalTime> findAvailableHours(Long doctorId, Long serviceId, LocalDate date) {
        com.example.server.repository.entity.Service service = serviceRepository.findById(serviceId).orElseThrow();
        int serviceDuration = service.getDuration();

        List<Appointment> appointments = appointmentRepository.findBookedTimesAndDurationsByDoctorIdAndDate(doctorId, date);
        return calculateAvailableHours(appointments, serviceDuration);
    }

    private List<LocalTime> calculateAvailableHours(List<Appointment> bookedAppointments, int duration) {
        List<LocalTime> availableHours = new ArrayList<>();
        LocalTime startOfWork = LocalTime.of(9, 0);
        LocalTime endOfWork = LocalTime.of(17, 0);
        LocalTime currentTime = startOfWork;

        if (bookedAppointments.isEmpty() && duration <= ChronoUnit.MINUTES.between(startOfWork, endOfWork)) {
            for (LocalTime time = startOfWork; time.plusMinutes(duration).isBefore(endOfWork.plusMinutes(1)); time = time.plusMinutes(30)) {
                availableHours.add(time);
            }
            return availableHours;
        }

        for (Appointment appointment : bookedAppointments) {
            while (currentTime.plusMinutes(duration).isBefore(appointment.getTime().plusMinutes(1)) && currentTime.plusMinutes(duration).isBefore(endOfWork.plusMinutes(1))) {
                availableHours.add(currentTime);
                currentTime = currentTime.plusMinutes(30); // Incrementăm la fiecare 30 de minute
            }

            currentTime = appointment.getTime().plusMinutes(appointment.getFinalDuration());
        }

        while (currentTime.plusMinutes(duration).isBefore(endOfWork.plusMinutes(1))) {
            availableHours.add(currentTime);
            currentTime = currentTime.plusMinutes(30);
        }

        return availableHours;
    }

    @Override
    public DoctorUpdateDTO updateDoctor(Long doctorId, DoctorUpdateDTO doctorUpdateDTO) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();

        if (doctorUpdateDTO.getFirstName() != null) {
            doctor.setFirstName(doctorUpdateDTO.getFirstName());
        }

        if (doctorUpdateDTO.getLastName() != null) {
            doctor.setLastName(doctorUpdateDTO.getLastName());
        }

        if (doctorUpdateDTO.getEmail() != null) {
            if (doctorRepository.findByEmail(doctorUpdateDTO.getEmail()).isEmpty()) {
                doctor.setEmail(doctorUpdateDTO.getEmail());
            }
            else {
                throw new EmailExistsException("Email is already in use");
            }
        }

        if (doctorUpdateDTO.getPhoneNo() != null) {
            doctor.setPhoneNo(doctorUpdateDTO.getPhoneNo());
        }

        if (doctorUpdateDTO.getAddress() != null) {
            doctor.setAddress(doctorUpdateDTO.getAddress());
        }

        if (doctorUpdateDTO.getDateOfEmployment() != null) {
            doctor.setDateOfEmployment(doctorUpdateDTO.getDateOfEmployment());
        }

        doctorRepository.save(doctor);
        return modelMapper.map(doctor, DoctorUpdateDTO.class);
    }

    @Override
    public List<TreatmentTypesDTO> getDoctorServicesInCurrentMonth(Long doctorId) {
        YearMonth currentMonth = YearMonth.now();
        LocalDate startOfMonth = currentMonth.atDay(1);
        LocalDate endOfMonth = currentMonth.atEndOfMonth();

        List<Appointment> appointments = appointmentRepository
                .findByDoctorIdAndDateBetween(doctorId, startOfMonth, endOfMonth);

        Map<String, Long> serviceCountMap = appointments.stream()
                .collect(Collectors.groupingBy(
                        appointment -> appointment.getService().getName(),
                        Collectors.counting()
                ));

        return serviceCountMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(entry -> new TreatmentTypesDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteDoctorById(Long doctorId) {
        doctorRepository.deleteById(doctorId);
        return true;
    }
}

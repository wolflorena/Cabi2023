package com.example.server.service.implementation;

import com.example.server.exception.types.EmailExistsException;
import com.example.server.exception.types.NotFoundException;
import com.example.server.repository.AppointmentRepository;
import com.example.server.repository.DTOs.*;
import com.example.server.repository.DoctorRepository;
import com.example.server.repository.DoctorUnavailabilityRepository;
import com.example.server.repository.ServiceRepository;
import com.example.server.repository.entity.Appointment;
import com.example.server.repository.entity.Doctor;
import com.example.server.repository.entity.DoctorUnavailability;
import com.example.server.service.DoctorService;
import com.example.server.service.SendEmailService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.*;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final ServiceRepository serviceRepository;
    private final DoctorUnavailabilityRepository doctorUnavailabilityRepository;
    private final SendEmailService sendEmailService;
    private final ModelMapper modelMapper;

    public DoctorServiceImpl(DoctorRepository doctorRepository,
                             AppointmentRepository appointmentRepository,
                             ServiceRepository serviceRepository,
                             DoctorUnavailabilityRepository doctorUnavailabilityRepository,
                             SendEmailService sendEmailService,
                             ModelMapper modelMapper) {

        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.serviceRepository = serviceRepository;
        this.doctorUnavailabilityRepository = doctorUnavailabilityRepository;
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
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new NotFoundException("Doctor not found"));
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
        com.example.server.repository.entity.Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new NotFoundException("Service not found"));

        for (LocalDate date = today; date.isBefore(end); date = date.plusDays(1)) {
            List<Appointment> bookedAppointments = appointmentRepository.findBookedTimesAndDurationsByDoctorIdAndDateAndStatusNot(
                    doctorId, date, Appointment.AppointmentStatus.CANCELLED);

            List<DoctorUnavailability> unavailabilities = doctorUnavailabilityRepository.findByDoctorId(doctorId);
            boolean isUnavailable = false;

            for (DoctorUnavailability unavailability : unavailabilities) {
                if (!date.isBefore(unavailability.getStartDate()) && !date.isAfter(unavailability.getEndDate())) {
                    if (unavailability.getStartTime() == null && unavailability.getEndTime() == null) {
                        isUnavailable = true;
                        break;
                    } else {
                        for (Appointment appointment : bookedAppointments) {
                            if (!appointment.getTime().isBefore(unavailability.getStartTime()) &&
                                    !appointment.getTime().isAfter(unavailability.getEndTime())) {
                                isUnavailable = true;
                                break;
                            }
                        }
                    }
                }
            }

            if (!isUnavailable && isDayAvailable(bookedAppointments, service.getDuration())) {
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

    @Override
    public List<LocalTime> findAvailableHours(Long doctorId, Long serviceId, LocalDate date) {
        com.example.server.repository.entity.Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new NotFoundException("Service not found"));
        int serviceDuration = service.getDuration();

        List<Appointment> appointments = appointmentRepository.findBookedTimesAndDurationsByDoctorIdAndDate(doctorId, date);
        List<DoctorUnavailability> unavailabilities = doctorUnavailabilityRepository.findByDoctorIdAndDate(doctorId, date);

        return calculateAvailableHours(appointments, serviceDuration, unavailabilities);
    }

    private List<LocalTime> calculateAvailableHours(List<Appointment> bookedAppointments, int duration, List<DoctorUnavailability> unavailabilities) {
        Set<LocalTime> availableHours = new TreeSet<>();
        LocalTime startOfWork = LocalTime.of(9, 0);
        LocalTime endOfWork = LocalTime.of(17, 0);
        LocalTime currentTime = startOfWork;

        while (currentTime.plusMinutes(duration).isBefore(endOfWork.plusMinutes(1))) {
            if (isTimeAvailable(currentTime, duration, bookedAppointments, unavailabilities)) {
                availableHours.add(currentTime);
            }
            currentTime = currentTime.plusMinutes(30);
        }

        return new ArrayList<>(availableHours);
    }

    private boolean isTimeAvailable(LocalTime time, int duration, List<Appointment> bookedAppointments, List<DoctorUnavailability> unavailabilities) {
        LocalTime endTime = time.plusMinutes(duration);

        // Verificăm dacă timpul se suprapune cu o programare existentă
        for (Appointment appointment : bookedAppointments) {
            LocalTime appointmentStartTime = appointment.getTime();
            LocalTime appointmentEndTime = appointment.getTime().plusMinutes(appointment.getFinalDuration());
            if (time.isBefore(appointmentEndTime) && endTime.isAfter(appointmentStartTime)) {
                return false;
            }
        }

        // Verificăm dacă timpul se suprapune cu o indisponibilitate
        for (DoctorUnavailability unavailability : unavailabilities) {
            if (unavailability.getStartTime() == null && unavailability.getEndTime() == null) {
                // Indisponibilitate pentru întreaga zi
                return false;
            } else if (unavailability.getStartTime() != null && unavailability.getEndTime() != null) {
                if (time.isBefore(unavailability.getEndTime()) && endTime.isAfter(unavailability.getStartTime())) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public DoctorUpdateDTO updateDoctor(Long doctorId, DoctorUpdateDTO doctorUpdateDTO) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(()-> new NotFoundException("Doctor not found"));

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

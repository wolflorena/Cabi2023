package com.example.server.service.implementation;

import com.example.server.exception.types.AppointmentExistsException;
import com.example.server.exception.types.DoctorUnavailableException;
import com.example.server.exception.types.NotFoundException;
import com.example.server.repository.*;
import com.example.server.repository.DTOs.*;
import com.example.server.repository.entity.Appointment;
import com.example.server.repository.entity.Customer;
import com.example.server.repository.entity.Doctor;
import com.example.server.repository.entity.DoctorUnavailability;
import com.example.server.service.AppointmentService;
import com.example.server.service.SendEmailService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    private static final Long ID_CLIENT_DEFAULT = (long) 11; //used when the admin makes an appointment, but the patient doesn't have an account
    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorUnavailabilityRepository doctorUnavailabilityRepository;
    private final ServiceRepository serviceRepository;
    private final SendEmailService sendEmailService;
    private final ModelMapper modelMapper;

    private AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                   CustomerRepository customerRepository ,
                                   DoctorRepository doctorRepository,
                                   DoctorUnavailabilityRepository doctorUnavailabilityRepository,
                                   ServiceRepository serviceRepository,
                                   SendEmailService sendEmailService,
                                   ModelMapper modelMapper){
        this.appointmentRepository = appointmentRepository;
        this.customerRepository = customerRepository;
        this.doctorRepository = doctorRepository;
        this.doctorUnavailabilityRepository = doctorUnavailabilityRepository;
        this.serviceRepository = serviceRepository;
        this.sendEmailService = sendEmailService;
        this.modelMapper = modelMapper;
    }

    @Override
    public AppointmentResponseDTO addAppointment(AppointmentRequestDTO appointmentRequestDTO){
        if(appointmentRepository.existsByDateAndTimeAndDoctorIdAndStatusNot(
                appointmentRequestDTO.getDate(),
                appointmentRequestDTO.getTime(),
                appointmentRequestDTO.getDoctorId(),
                Appointment.AppointmentStatus.CANCELLED
        )){
            throw new AppointmentExistsException("An appointment already exists at this date and time.");
        }

        List<DoctorUnavailability> unavailabilities = doctorUnavailabilityRepository.findByDoctorId(appointmentRequestDTO.getDoctorId());
        LocalDate appointmentDate = appointmentRequestDTO.getDate();
        LocalTime appointmentTime = appointmentRequestDTO.getTime();

        for (DoctorUnavailability unavailability : unavailabilities) {
            boolean isDateConflict = !appointmentDate.isBefore(unavailability.getStartDate()) &&
                    !appointmentDate.isAfter(unavailability.getEndDate());

            if (isDateConflict) {
                if (unavailability.getStartTime() == null && unavailability.getEndTime() == null) {
                    throw new DoctorUnavailableException("Doctor is unavailable for the entire day.");
                } else if (unavailability.getStartTime() != null && unavailability.getEndTime() != null) {
                    boolean isTimeConflict = !appointmentTime.isBefore(unavailability.getStartTime()) &&
                            !appointmentTime.isAfter(unavailability.getEndTime());

                    if (isTimeConflict) {
                        throw new DoctorUnavailableException("Doctor is unavailable at the requested time.");
                    }
                }
            }
        }

        Appointment appointment = new Appointment();

        if (appointmentRequestDTO.getCustomerId() == null) {
            appointmentRequestDTO.setCustomerId(ID_CLIENT_DEFAULT);
        }

        Customer customer = customerRepository.findById(appointmentRequestDTO.getCustomerId()).orElseThrow(() -> new NotFoundException("Patient not found"));
        Doctor doctor = doctorRepository.findById(appointmentRequestDTO.getDoctorId()).orElseThrow(() -> new NotFoundException("Doctor not found"));
        com.example.server.repository.entity.Service service = serviceRepository.findById(appointmentRequestDTO.getServiceId()).orElseThrow(() -> new NotFoundException("Service not found"));

        appointment.setCustomer(customer);
        appointment.setDoctor(doctor);
        appointment.setService(service);
        appointment.setDate(appointmentRequestDTO.getDate());
        appointment.setTime(appointmentRequestDTO.getTime());
        appointment.setFinalDuration(service.getDuration());
        appointment.setStatus(Appointment.AppointmentStatus.REQUESTED);

        customer.getAppointments().add(appointment);
        sendEmailService.sendAppointmentRequestInformationToPatient(appointmentRequestDTO.getCustomerId(), appointment);
        return modelMapper.map(appointmentRepository.save(appointment), AppointmentResponseDTO.class);
    }

    @Override
    public AppointmentDetailDTO getAppointmentById(Long appointmentId){
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Appointment not found"));
        return modelMapper.map(appointment, AppointmentDetailDTO.class);
    }

    @Override
    public Boolean deleteAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new NotFoundException("Appointment not found"));
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
        List<Appointment.AppointmentStatus> statuses = Arrays.asList(Appointment.AppointmentStatus.SCHEDULED,
                Appointment.AppointmentStatus.COMPLETED);
        List<Appointment> appointments = appointmentRepository.findAllByStatusIn(statuses);
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

        Pageable sortedByDateAndTime = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by("date").descending().and(Sort.by("time").descending()));

        List<Appointment.AppointmentStatus> statusList = (status == Appointment.AppointmentStatus.COMPLETED) ?
                Arrays.asList(Appointment.AppointmentStatus.COMPLETED, Appointment.AppointmentStatus.CANCELLED) :
                Arrays.asList(status);

        if(doctorIds == null || doctorIds.isEmpty()) {
            if(status == null) {
                appointments = appointmentRepository.findAll(sortedByDateAndTime);
            } else {
                appointments = appointmentRepository.findAllByStatusIn(statusList, sortedByDateAndTime);
            }
        } else {
            if(status == null) {
                appointments = appointmentRepository.findAllByDoctorIdIn(doctorIds, sortedByDateAndTime);
            } else {
                appointments = appointmentRepository.findAllByDoctorIdInAndStatusIn(doctorIds, statusList, sortedByDateAndTime);
            }
        }

        List<AppointmentAdminDTO> result = appointments.getContent()
                .stream()
                .map(appointment -> modelMapper.map(appointment, AppointmentAdminDTO.class))
                .collect(Collectors.toList());

        appointmentPageDTO.setTotal((int) appointments.getTotalElements());
        appointmentPageDTO.setPagedAppointments(new PageImpl<>(result, sortedByDateAndTime, appointments.getTotalElements()));
        return appointmentPageDTO;
    }

    @Override
    public List<AppointmentResponseDTO> getAllAppointmentsByDateAndDoctor(LocalDate date, Long doctorId){
        List<Appointment> appointments = appointmentRepository.findByDateAndDoctorId(date,doctorId);
        return appointments.stream()
                .map(appointment -> modelMapper.map(appointment, AppointmentResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public HistoryPageDTO getHistoryByCustomerId(Long customerId, Pageable pageable){
        HistoryPageDTO historyPageDTO = new HistoryPageDTO();
        Page<Appointment> appointments = appointmentRepository
                .findAllByCustomerIdOrderByDateTimeDesc(customerId,PageRequest.of(pageable.getPageNumber(),pageable.getPageSize()));

        historyPageDTO.setTotal((int) appointments.getTotalElements());

        List<AppointmentHistoryDTO> result = appointments.getContent()
                .stream()
                .map(appointment -> modelMapper.map(appointment, AppointmentHistoryDTO.class)).toList();
        return new HistoryPageDTO(historyPageDTO.getTotal(), new PageImpl<>(result,pageable,result.size()));
    }

    @Override
    public AppointmentResponseDTO updateAppointmentStatus(Long appointmentId, Appointment.AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new NotFoundException("Appointment not found"));

        appointment.setStatus(status);
        appointmentRepository.save(appointment);

        if (status == Appointment.AppointmentStatus.SCHEDULED) {
            sendEmailService.sendAppointmentInformationToPatient(appointment.getCustomer().getId(), appointment);
        }

        if (status == Appointment.AppointmentStatus.CANCELLED) {
            sendEmailService.sendAppointmentCancelledToPatient(appointment.getCustomer().getId(), appointment);
        }

        return modelMapper.map(appointment, AppointmentResponseDTO.class);
    }

    @Override
    public List<AppointmentDoctorDashboardDTO> getNextAppointments(Long doctorId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        LocalTime currentTime = now.toLocalTime();

        List<Appointment> appointments = appointmentRepository.findNextAppointmentsByDoctorId(doctorId,today, currentTime).
                orElseThrow(() -> new NotFoundException("There are no upcoming appointments for today"));

        return appointments.stream()
                .map(appointment -> modelMapper.map(appointment, AppointmentDoctorDashboardDTO.class))
                .limit(2)
                .collect(Collectors.toList());
    }

    @Override
    public int getAppointmentsNumberByStatusAndDoctor(Long doctorId, Appointment.AppointmentStatus appointmentStatus) {
        YearMonth currentMonth = YearMonth.now();
        LocalDate startOfMonth = currentMonth.atDay(1);
        LocalDate endOfMonth = currentMonth.atEndOfMonth();

        List<Appointment> appointments = appointmentRepository.findAllByDoctorIdAndStatusAndDateBetween(doctorId,
                appointmentStatus,
                startOfMonth,
                endOfMonth);
        return appointments.size();
    }

    @Override
    public List<WeeklyAppointmentsDTO> getWeeklyAppointments(Long doctorId) {
        YearMonth currentMonth = YearMonth.now();
        LocalDate startOfMonth = currentMonth.atDay(1);
        LocalDate endOfMonth = currentMonth.atEndOfMonth();

        List<Appointment> appointments = appointmentRepository.findAllByDoctorIdAndStatusAndDateBetween(doctorId,
                Appointment.AppointmentStatus.COMPLETED,
                startOfMonth,
                endOfMonth);

        Map<Integer, Long> weeklyCounts = appointments.stream()
                .collect(Collectors.groupingBy(appointment -> appointment.getDate().get(ChronoField.ALIGNED_WEEK_OF_MONTH), Collectors.counting()));

        List<WeeklyAppointmentsDTO> result = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            result.add(new WeeklyAppointmentsDTO("Week " + i, weeklyCounts.getOrDefault(i, 0L)));
        }

        return result;
    }

    @Override
    public List<AppointmentResponseDTO> getAppointmentsByDoctorAndViewType(Long doctorId, LocalDate date, String viewType) {
        LocalDate startDate;
        LocalDate endDate;

        switch (viewType.toUpperCase()) {
            case "DAY":
                startDate = date;
                endDate = date;
                break;
            case "WEEK":
                startDate = date.with(DayOfWeek.MONDAY);
                endDate = date.with(DayOfWeek.SUNDAY);
                break;
            case "MONTH":
                startDate = date.withDayOfMonth(1);
                endDate = date.withDayOfMonth(date.lengthOfMonth());
                break;
            default:
                throw new IllegalArgumentException("Invalid view type: " + viewType);
        }

        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndDateRange(doctorId, startDate, endDate);
        return appointments.stream()
                .map(appointment -> modelMapper.map(appointment, AppointmentResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentResponseDTO rescheduleAppointment(Long appointmentId, UpdateAppointmentDTO updateAppointmentDTO) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Appointment not found"));

        if (appointmentRepository.existsByDateAndTimeAndDoctorIdAndStatusNotAndAppointmentIdNot(
                updateAppointmentDTO.getDate(),
                updateAppointmentDTO.getTime(),
                updateAppointmentDTO.getDoctorId(),
                Appointment.AppointmentStatus.CANCELLED,
                appointmentId
        )) {
            throw new AppointmentExistsException("An appointment already exists at this date and time.");
        }

        List<DoctorUnavailability> unavailabilities = doctorUnavailabilityRepository.findByDoctorId(updateAppointmentDTO.getDoctorId());
        LocalDate appointmentDate = updateAppointmentDTO.getDate();
        LocalTime appointmentTime = updateAppointmentDTO.getTime();

        for (DoctorUnavailability unavailability : unavailabilities) {
            boolean isDateConflict = !appointmentDate.isBefore(unavailability.getStartDate()) &&
                    !appointmentDate.isAfter(unavailability.getEndDate());

            if (isDateConflict) {
                if (unavailability.getStartTime() == null && unavailability.getEndTime() == null) {
                    throw new DoctorUnavailableException("Doctor is unavailable for the entire day.");
                } else if (unavailability.getStartTime() != null && unavailability.getEndTime() != null) {
                    boolean isTimeConflict = !appointmentTime.isBefore(unavailability.getStartTime()) &&
                            !appointmentTime.isAfter(unavailability.getEndTime());

                    if (isTimeConflict) {
                        throw new DoctorUnavailableException("Doctor is unavailable at the requested time.");
                    }
                }
            }
        }

        Doctor doctor = doctorRepository.findById(updateAppointmentDTO.getDoctorId())
                .orElseThrow(() -> new NotFoundException("Doctor not found"));
        com.example.server.repository.entity.Service service = serviceRepository.findById(updateAppointmentDTO.getServiceId())
                .orElseThrow(() -> new NotFoundException("Service not found"));

        appointment.setDoctor(doctor);
        appointment.setService(service);
        appointment.setDate(updateAppointmentDTO.getDate());
        appointment.setTime(updateAppointmentDTO.getTime());
        appointment.setFinalDuration(service.getDuration());
        appointment.setStatus(Appointment.AppointmentStatus.REQUESTED);

        return modelMapper.map(appointmentRepository.save(appointment), AppointmentResponseDTO.class);
    }

}

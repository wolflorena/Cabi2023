package com.example.server.config;

import com.example.server.repository.AppointmentRepository;
import com.example.server.repository.CustomerRepository;
import com.example.server.repository.DoctorRepository;
import com.example.server.repository.ServiceRepository;
import com.example.server.repository.entity.Appointment;
import com.example.server.service.SendEmailService;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class AppointmentScheduler {
    private AppointmentRepository appointmentRepository;
    private final SendEmailService sendEmailService;

    private AppointmentScheduler(AppointmentRepository appointmentRepository, SendEmailService sendEmailService) {
        this.appointmentRepository = appointmentRepository;
        this.sendEmailService = sendEmailService;
    }

    // it runs every day
    @Scheduled(cron = "0 0 0 * * ?")
    public void updatePastDueAppointments() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        List<Appointment> appointments = appointmentRepository.findPastDueAppointments(today, now);
        for (Appointment appointment : appointments) {
            if (appointment.getStatus().equals(Appointment.AppointmentStatus.SCHEDULED)) {
                appointment.setStatus(Appointment.AppointmentStatus.COMPLETED);
                appointmentRepository.save(appointment);
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void deletePastDueRequestedAppointments() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        List<Appointment> appointments = appointmentRepository.findPastDueRequestedAppointments(today, now);
        for (Appointment appointment : appointments) {
            if (appointment.getStatus().equals(Appointment.AppointmentStatus.REQUESTED)) {
                sendEmailService.sendAppointmentCancelledToPatient(appointment.getCustomer().getId(), appointment);
                appointmentRepository.delete(appointment);
            }
        }
    }
}

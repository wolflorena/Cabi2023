package com.example.server.config;

import com.example.server.repository.AppointmentRepository;
import com.example.server.repository.CustomerRepository;
import com.example.server.repository.DoctorRepository;
import com.example.server.repository.ServiceRepository;
import com.example.server.repository.entity.Appointment;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class AppointmentScheduler {
    private AppointmentRepository appointmentRepository;

    private AppointmentScheduler(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    // it runs every hour
    @Scheduled(cron = "0 0 * * * ?")
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
}

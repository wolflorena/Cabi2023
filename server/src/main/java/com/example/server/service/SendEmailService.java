package com.example.server.service;

import com.example.server.repository.entity.Appointment;

public interface SendEmailService {
    void sendPasswordToDoctor(Long doctorId);
    void sendAppointmentInformationToPatient(Long customerId, Appointment appointment);
    void sendAppointmentRequestInformationToPatient(Long customerId, Appointment appointment);
    void sendAppointmentCancelledToPatient(Long customerId, Appointment appointment);
}

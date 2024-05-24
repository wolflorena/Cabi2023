package com.example.server.repository.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class AppointmentDoctorDashboardDTO {
    private Long appointmentId;
    private LocalDate date;
    private LocalTime time;
    private String customerFirstName;
    private String customerLastName;
    private String serviceName;

    @Data
    @NoArgsConstructor
    public static class CustomerAdminDTO {
        private Long customerId;
        private String firstName;
        private String lastName;
        private String email;
        private String phoneNo;
    }
}

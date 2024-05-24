package com.example.server.repository.DTOs.Customers;

import com.example.server.repository.DTOs.AppointmentDoctorDashboardDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPageDTO {
    private int total;
    private Page<AppointmentDoctorDashboardDTO.CustomerAdminDTO> pagedCustomers;
}

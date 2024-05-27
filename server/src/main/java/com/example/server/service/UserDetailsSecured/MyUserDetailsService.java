package com.example.server.service.UserDetailsSecured;

import com.example.server.exception.types.AccountDeactivatedException;
import com.example.server.repository.AdminRepository;
import com.example.server.repository.CustomerRepository;
import com.example.server.repository.DoctorRepository;
import com.example.server.repository.entity.Admin;
import com.example.server.repository.entity.CustomUserDetails;
import com.example.server.repository.entity.Customer;
import com.example.server.repository.entity.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Check if the user is a customer
        Optional<Customer> customerOpt = customerRepository.findByEmail(username);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            if (customer.getAccountStatus() == Customer.AccountStatus.INACTIVE) {
                throw new AccountDeactivatedException("Account is deactivated");
            }
            return new CustomUserDetails(customer.getId(), customer.getEmail(), customer.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_CUSTOMER")));
        }

        // Check if the user is an admin
        Optional<Admin> adminOpt = adminRepository.findByEmail(username);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            return new CustomUserDetails(admin.getId(), admin.getEmail(), admin.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        }

        // Check if the user is a doctor
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(username);
        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            return new CustomUserDetails(doctor.getId(), doctor.getEmail(), doctor.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_DOCTOR")));
        }

        // If no user found in any repository
        throw new UsernameNotFoundException("User not found with email: " + username);
    }
}

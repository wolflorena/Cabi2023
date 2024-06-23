package com.example.server.repository;

import com.example.server.repository.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private ServiceRepository serviceRepository;

    @Test
    public void testSaveAndRetrieveCustomer() {
        Customer savedCustomer = new Customer();
        savedCustomer.setFirstName("customer1");
        savedCustomer.setAccountStatus(Customer.AccountStatus.ACTIVE);
        savedCustomer.setEmail("customer1@example.com");
        savedCustomer.setPhoneNo("1234567890");
        customerRepository.save(savedCustomer);

        Customer retrievedCustomer = customerRepository.findById(savedCustomer.getId()).orElse(null);

        assertNotNull(retrievedCustomer);
        assertEquals("customer1", retrievedCustomer.getFirstName());
        assertEquals(Customer.AccountStatus.ACTIVE, retrievedCustomer.getAccountStatus());
        assertEquals("customer1@example.com", retrievedCustomer.getEmail());
        assertEquals("1234567890", retrievedCustomer.getPhoneNo());
    }

    @Test
    public void testExistsByEmail() {
        Customer customer = new Customer();
        customer.setEmail("test@example.com");
        customerRepository.save(customer);

        assertTrue(customerRepository.existsByEmail("test@example.com"));
        assertFalse(customerRepository.existsByEmail("nonexistent@example.com"));
    }

    @Test
    public void testFindByEmail() {
        Customer customer = new Customer();
        customer.setEmail("findme@example.com");
        customerRepository.save(customer);

        Customer foundCustomer = customerRepository.findByEmail("findme@example.com").orElse(null);

        assertNotNull(foundCustomer);
        assertEquals("findme@example.com", foundCustomer.getEmail());
    }

    @Test
    public void testExistsByPhoneNo() {
        Customer customer = new Customer();
        customer.setPhoneNo("9876543210");
        customerRepository.save(customer);

        assertTrue(customerRepository.existsByPhoneNo("9876543210"));
        assertFalse(customerRepository.existsByPhoneNo("0000000000"));
    }

    @Test
    public void testFindCustomersByDoctorIdAndAppointmentStatus() {
        Customer customer = new Customer();
        customer.setFirstName("Test Customer");
        customerRepository.save(customer);

        Doctor doctor = new Doctor();
        doctor.setFirstName("Test Doctor");
        doctorRepository.save(doctor);

        Service service = new Service();
        service.setName("Test Service");
        serviceRepository.save(service);

        Appointment appointment = new Appointment();
        appointment.setCustomer(customer);
        appointment.setDoctor(doctor);
        appointment.setStatus(Appointment.AppointmentStatus.SCHEDULED);
        appointment.setService(service);
        appointment.setDate(LocalDate.now());
        appointment.setFinalDuration(60);
        appointment.setTime(LocalTime.now());
        appointmentRepository.save(appointment);

        List<Customer> customers = customerRepository.findCustomersByDoctorIdAndAppointmentStatus(doctor.getId());

        assertFalse(customers.isEmpty());
        assertEquals("Test Customer", customers.get(0).getFirstName());
    }

    @Test
    public void testFindAllByStatusNot() {
        Customer activeCustomer = new Customer();
        activeCustomer.setFirstName("Active Customer");
        activeCustomer.setAccountStatus(Customer.AccountStatus.ACTIVE);
        customerRepository.save(activeCustomer);

        Customer inactiveCustomer = new Customer();
        inactiveCustomer.setFirstName("Inactive Customer");
        inactiveCustomer.setAccountStatus(Customer.AccountStatus.INACTIVE);
        customerRepository.save(inactiveCustomer);

        List<Customer> customers = customerRepository.findAllByStatusNot(Customer.AccountStatus.INACTIVE, PageRequest.of(0, 10)).getContent();

        assertFalse(customers.isEmpty());
        assertEquals("Active Customer", customers.get(0).getFirstName());
        assertNotEquals(Customer.AccountStatus.INACTIVE, customers.get(0).getAccountStatus());
    }
}

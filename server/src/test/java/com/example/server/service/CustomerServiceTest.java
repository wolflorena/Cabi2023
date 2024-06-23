package com.example.server.service;

import com.example.server.exception.types.EmailExistsException;
import com.example.server.repository.CustomerRepository;
import com.example.server.repository.DTOs.Customers.RegisterCustomerDTO;
import com.example.server.repository.DTOs.Customers.ResponseCustomerDTO;
import com.example.server.repository.entity.Customer;
import com.example.server.service.implementation.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ActiveProfiles("test")
public class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private CustomerServiceImpl customerService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testRegisterCustomer_Success() throws Exception {
        RegisterCustomerDTO registerCustomerDto = new RegisterCustomerDTO();
        registerCustomerDto.setFirstName("John");
        registerCustomerDto.setLastName("Doe");
        registerCustomerDto.setEmail("john.doe@example.com");
        registerCustomerDto.setPassword("Password123!");

        Customer customer = new Customer(registerCustomerDto);
        customer.setId(1L);

        when(customerRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(modelMapper.map(any(Customer.class), eq(ResponseCustomerDTO.class))).thenReturn(new ResponseCustomerDTO());

        ResponseCustomerDTO response = customerService.register(registerCustomerDto);

        assertNotNull(response);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testRegisterCustomer_EmailExists() {
        RegisterCustomerDTO registerCustomerDto = new RegisterCustomerDTO();
        registerCustomerDto.setFirstName("John");
        registerCustomerDto.setLastName("Doe");
        registerCustomerDto.setEmail("john.doe@example.com");
        registerCustomerDto.setPassword("password");

        when(customerRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(EmailExistsException.class, () -> customerService.register(registerCustomerDto));
    }

    @Test
    void testGetById_Success() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(modelMapper.map(any(Customer.class), eq(ResponseCustomerDTO.class))).thenReturn(new ResponseCustomerDTO());

        ResponseCustomerDTO response = customerService.getById(1L);

        assertNotNull(response);
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_NotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> customerService.getById(1L));
    }

    @Test
    void testChangePassword_Success() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setPassword("encodedOldPassword123!");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedOldPassword123!");

        customerService.changePassword(1L, "oldPassword", "newPassword");

        verify(customerRepository, times(1)).save(customer);
        assertEquals("encodedOldPassword123!", customer.getPassword());
    }

    @Test
    void testChangePassword_WrongCurrentPassword() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setPassword("encodedOldPassword");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> customerService.changePassword(1L, "wrongPassword", "newPassword"));
    }

    @Test
    void testDeleteAccount_Success() {
        Customer customer = new Customer();
        customer.setId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        customerService.deleteAccount(1L);

        verify(customerRepository, times(1)).delete(customer);
    }

    @Test
    void testDeleteAccount_NotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> customerService.deleteAccount(1L));
    }
}

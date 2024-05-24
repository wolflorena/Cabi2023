package com.example.server.service.implementation;

import com.example.server.exception.types.EmailExistsException;
import com.example.server.repository.DTOs.AppointmentDoctorDashboardDTO;
import com.example.server.repository.DTOs.Customers.*;
import com.example.server.repository.entity.Customer;
import com.example.server.repository.CustomerRepository;
import com.example.server.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseCustomerDTO register(RegisterCustomerDTO registerCustomerDto){
        registerCustomerDto.setEmail(registerCustomerDto.getEmail().toLowerCase());
        if(emailExists(registerCustomerDto.getEmail())){
            throw new EmailExistsException("Email already in use");
        }

        String encodedPassword = passwordEncoder.encode(registerCustomerDto.getPassword());
        registerCustomerDto.setPassword(encodedPassword);

        Customer customerToBeSaved = new Customer(registerCustomerDto);
        //add a default profile picture for every new user.
        byte[] defaultAvatar = new byte[0];

        //getFile() method needs this try catch check.
        try {
            defaultAvatar = loadDefaultAvatar();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        customerToBeSaved.setAvatar(defaultAvatar);

        customerRepository.save(customerToBeSaved);
        return modelMapper.map(customerToBeSaved, ResponseCustomerDTO.class);
    }

    public ResponseCustomerDTO getById(Long id){
        Customer returnedCustomer = customerRepository.findById(id).orElse(null);
        if(returnedCustomer==null){
            //TODO THROW EXCEPTION
        }

        return modelMapper.map(returnedCustomer, ResponseCustomerDTO.class);
    }

    public List<ResponseCustomerDTO> getAll(){
        return customerRepository
                .findAll()
                .stream()
                .map( user -> modelMapper.map(user, ResponseCustomerDTO.class))
                .toList();
    }

    public void deleteById(Long id){
        customerRepository.deleteById(id);
    }

    public boolean emailExists(String email) {
        String lowerCaseEmail = email.toLowerCase();
        return customerRepository.existsByEmail(lowerCaseEmail);
    }

    @Override
    public CustomerPageDTO getAllCustomersForAdmin(Pageable pageable) {
        CustomerPageDTO customerPageDTO = new CustomerPageDTO();
        customerPageDTO.setTotal(customerRepository.findAll().size());
        List<AppointmentDoctorDashboardDTO.CustomerAdminDTO> result = customerRepository
                .findAll()
                .stream()
                .map(customer -> modelMapper.map(customer, AppointmentDoctorDashboardDTO.CustomerAdminDTO.class))
                .toList();
        return new CustomerPageDTO(customerPageDTO.getTotal(), new PageImpl<>(result, pageable, result.size()));
    }

    @Override
    public ResponseCustomerDTO editAccountStatus(Long customerId, Customer.AccountStatus status) {
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        customer.setAccountStatus(status);
        customerRepository.save(customer);
        return modelMapper.map(customer, ResponseCustomerDTO.class);
    }

    @Override
    public CustomerEditDetailsDTO editCustomerDetails(Long customerId, CustomerEditDetailsDTO customerDetails){
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new IllegalArgumentException("Customer not found"));;

        if (customerDetails.getEmail() != null && !isValidEmail(customerDetails.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }

        // check the email not to be already used for another user.
        if (customerDetails.getEmail() != null && !customerDetails.getEmail().equals(customer.getEmail())) {
            boolean emailExists = customerRepository.existsByEmail(customerDetails.getEmail());
            if (emailExists) {
                throw new IllegalArgumentException("Email already in use");
            }
        }

        if (customerDetails.getFirstName() != null) {
            customer.setFirstName(customerDetails.getFirstName());
        }
        if (customerDetails.getLastName() != null) {
            customer.setLastName(customerDetails.getLastName());
        }
        if (customerDetails.getEmail() != null) {
            customer.setEmail(customerDetails.getEmail());
        }
        if (customerDetails.getPhoneNo() != null) {
            customer.setPhoneNo(customerDetails.getPhoneNo());
        }
        if (customerDetails.getDateOfBirth() != null) {
            customer.setDateOfBirth(customerDetails.getDateOfBirth());
        }
        if (customerDetails.getOccupation() != null) {
            customer.setOccupation(customerDetails.getOccupation());
        }
        customer = customerRepository.save(customer);

        // Step 6: Return the updated customer details
        return new CustomerEditDetailsDTO(customer);
    }

    public Customer uploadAvatar(Long customerId, MultipartFile avatar) {
        try{
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found!"));
            customer.setAvatar(avatar.getBytes());
            return customerRepository.save(customer);
        }catch (Exception e){
            throw new IllegalArgumentException(e);
        }
    }

    public byte[] getAvatar(Long customerId){
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return customer.getAvatar();
    }

    private boolean isValidEmail(String email) {
        // simple regex rule for email validation.
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private byte[] loadDefaultAvatar() throws IOException {
        ClassPathResource imgFile = new ClassPathResource("images/default-avatar.png");
        return Files.readAllBytes(imgFile.getFile().toPath());
    }
}

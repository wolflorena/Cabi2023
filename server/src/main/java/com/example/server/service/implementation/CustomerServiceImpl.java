package com.example.server.service.implementation;

import com.example.server.exception.types.EmailExistsException;
import com.example.server.repository.DTOs.RegisterCustomerDTO;
import com.example.server.repository.DTOs.ResponseCustomerDTO;
import com.example.server.repository.entity.Customer;
import com.example.server.repository.CustomerRepository;
import com.example.server.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
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
        Customer customerToBeSaved = new Customer(registerCustomerDto);
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
}

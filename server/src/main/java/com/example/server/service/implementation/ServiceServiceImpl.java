package com.example.server.service.implementation;

import com.example.server.repository.ServiceRepository;
import com.example.server.repository.entity.Service;
import com.example.server.service.ServiceService;
import org.modelmapper.ModelMapper;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {
    private final ServiceRepository serviceRepository;
    private final ModelMapper modelMapper;

    public ServiceServiceImpl(ServiceRepository serviceRepository, ModelMapper modelMapper){
        this.serviceRepository = serviceRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Service addService(Service service) {
        serviceRepository.save(service);
        return service;
    }
}

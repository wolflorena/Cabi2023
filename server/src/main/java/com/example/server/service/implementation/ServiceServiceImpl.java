package com.example.server.service.implementation;

import com.example.server.exception.types.NotFoundException;
import com.example.server.repository.ServiceRepository;
import com.example.server.repository.entity.Service;
import com.example.server.service.ServiceService;
import org.modelmapper.ModelMapper;

import java.util.List;

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

    @Override
    public Service getService(Long serviceId){
        Service service = serviceRepository.findById(serviceId).orElseThrow(()-> new NotFoundException("Service not found"));
        return service;
    }

    @Override
    public List<Service> getAll() {
        return serviceRepository.findAll();
    }
}

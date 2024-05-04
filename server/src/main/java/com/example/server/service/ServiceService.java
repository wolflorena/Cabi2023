package com.example.server.service;

import com.example.server.repository.entity.Service;

import java.util.List;

public interface ServiceService {
    Service addService(Service service);
    Service getService(Long serviceId);
    List<Service> getAll();
}

package com.example.server.service;

import com.example.server.repository.entity.Service;

public interface ServiceService {
    Service addService(Service service);
    Service getService(Long serviceId);
}

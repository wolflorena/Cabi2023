package com.example.server.controller;

import com.example.server.repository.entity.Service;
import com.example.server.service.ServiceService;
import com.example.server.service.implementation.ServiceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/services")
public class ServiceController {
    @Autowired
    private ServiceService serviceService;

    public ServiceController(ServiceService serviceService){
        this.serviceService = serviceService;
    }

    @PostMapping("/addService")
    public ResponseEntity<Object> addService(@RequestBody Service service){
        return new ResponseEntity<>(serviceService.addService(service), HttpStatus.OK);
    }

    @GetMapping("/getService")
    public ResponseEntity<Object> getService(@RequestParam Long serviceId){
        return new ResponseEntity<>(serviceService.getService(serviceId), HttpStatus.OK);
    }
}

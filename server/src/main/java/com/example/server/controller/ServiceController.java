package com.example.server.controller;

import com.example.server.repository.entity.Service;
import com.example.server.service.implementation.ServiceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/services")
public class ServiceController {
    @Autowired
    private ServiceServiceImpl serviceServiceImpl;

    public ServiceController(ServiceServiceImpl serviceServiceImpl){
        this.serviceServiceImpl = serviceServiceImpl;
    }

    @PostMapping("/addService")
    public ResponseEntity<Object> addService(@RequestBody Service service){
        return new ResponseEntity<>(serviceServiceImpl.addService(service), HttpStatus.OK);
    }
}

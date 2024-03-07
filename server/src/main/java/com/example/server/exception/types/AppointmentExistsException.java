package com.example.server.exception.types;

public class AppointmentExistsException extends RuntimeException {
    public AppointmentExistsException(String message) {
        super(message);
    }
}
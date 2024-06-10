package com.example.server.exception.types;

public class CouldNotRetrieveAppointmentsByDateAndTypeException extends RuntimeException{
    public CouldNotRetrieveAppointmentsByDateAndTypeException(String message) {
        super(message);
    }
}

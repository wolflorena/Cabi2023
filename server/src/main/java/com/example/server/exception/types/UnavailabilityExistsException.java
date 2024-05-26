package com.example.server.exception.types;

public class UnavailabilityExistsException extends RuntimeException{
    public UnavailabilityExistsException(String message) {
        super(message);
    }
}

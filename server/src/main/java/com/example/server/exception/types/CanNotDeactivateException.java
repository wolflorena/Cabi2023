package com.example.server.exception.types;

public class CanNotDeactivateException extends RuntimeException{
    public CanNotDeactivateException(String message) {
        super(message);
    }
}

package com.example.server.exception.types;

public class FormEventAlreadyExistsException extends RuntimeException {
    public FormEventAlreadyExistsException(String message){
        super(message);
    }
}

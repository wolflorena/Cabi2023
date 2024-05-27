package com.example.server.exception.types;

public class AccountDeactivatedException extends RuntimeException{
    public AccountDeactivatedException(String message) {
        super(message);
    }

}

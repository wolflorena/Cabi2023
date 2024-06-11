package com.example.server.exception.types;

public class CanNotRetrieveHistory extends RuntimeException{
    public CanNotRetrieveHistory(String message) {
        super(message);
    }
}

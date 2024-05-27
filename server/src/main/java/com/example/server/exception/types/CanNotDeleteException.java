package com.example.server.exception.types;

public class CanNotDeleteException extends RuntimeException{
    public CanNotDeleteException(String message) {
        super(message);
    }
}

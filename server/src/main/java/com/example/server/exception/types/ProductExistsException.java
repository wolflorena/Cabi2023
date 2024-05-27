package com.example.server.exception.types;

public class ProductExistsException extends RuntimeException{
    public ProductExistsException(String message) {
        super(message);
    }
}

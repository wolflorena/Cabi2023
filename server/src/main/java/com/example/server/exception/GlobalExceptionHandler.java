package com.example.server.exception;

import com.example.server.exception.types.EmailExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<Object> handleEmailExistsException(EmailExistsException ex, WebRequest request) {
        // Create a response structure
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Email already in use");

        // Return response entity with status code
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}

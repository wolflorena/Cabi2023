package com.example.server.exception;

import com.example.server.exception.types.AccountDeactivatedException;
import com.example.server.exception.types.AppointmentExistsException;
import com.example.server.exception.types.BadCredentialsException;
import com.example.server.exception.types.EmailExistsException;
import com.example.server.exception.types.*;
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
    private ResponseEntity<Object> buildResponseEntity(HttpStatus status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<Object> handleEmailExistsException(EmailExistsException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, "Email already in use");
    }

    @ExceptionHandler(AppointmentExistsException.class)
    public ResponseEntity<Object> handleAppointmentExistsException(AppointmentExistsException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.CONFLICT, "An appointment already exists at this date and time for the selected doctor.");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.CONFLICT, "Bad credentials");
    }

    @ExceptionHandler(DoctorUnavailableException.class)
    public ResponseEntity<Object> handleDoctorUnavailableException(DoctorUnavailableException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(ProductExistsException.class)
    public ResponseEntity<Object> handleProductExistsException(ProductExistsException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(UnavailabilityExistsException.class)
    public ResponseEntity<Object> handleUnavailabilityExistsException(UnavailabilityExistsException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(AccountDeactivatedException.class)
    public ResponseEntity<Object> handleAccountDeactivatedException(AccountDeactivatedException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Account is deactivated");

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(CouldNotRetrieveAppointmentsByDateAndTypeException.class)
    public ResponseEntity<Object> CouldNotRetrieveAppointmentsByDateAndTypeException (CouldNotRetrieveAppointmentsByDateAndTypeException  ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Can not retrieve appointments based on type and date");

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(FormEventAlreadyExistsException.class)
    public ResponseEntity<Object> handleFormEventAlreadyExistsException(FormEventAlreadyExistsException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<Object> handleIncorrectPasswordException(IncorrectPasswordException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}

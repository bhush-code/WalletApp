package com.bhushan.walletapp.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex)
    {
        Map<String,Object> exceptionBody=new HashMap<>();
        exceptionBody.put("timestamp", LocalDateTime.now());
        exceptionBody.put("error","Internal Server error");
        exceptionBody.put("Message",ex.getMessage());
        return new ResponseEntity<>(exceptionBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleFieldValidation(MethodArgumentNotValidException ex)
    {
        Map<String,String> fielderrors=new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err->fielderrors.put(err.getField(),err.getDefaultMessage()));
        Map<String, Object> exceptionBody=new HashMap<>();
        exceptionBody.put("timestamp",LocalDateTime.now());
        exceptionBody.put("error","field validations failed");
        exceptionBody.put("details", fielderrors);
        return new ResponseEntity<>(exceptionBody,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintVoilation(ConstraintViolationException ex)
    {
        Map<String, Object> error=new HashMap<>();
        error.put("timeStamp",LocalDateTime.now());
        error.put("error",ex.getMessage());
        return  new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleConstraintVoilation(MethodArgumentTypeMismatchException ex)
    {
        Map<String, Object> error=new HashMap<>();
        error.put("timeStamp",LocalDateTime.now());
//        error.put("detailed_error",ex.getMessage());
        error.put("error","User id cannot contain characters");
        return  new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex)
    {
        Map<String, Object> error=new HashMap<>();
        error.put("timeStamp",LocalDateTime.now());
        error.put("error",ex.getMessage());
        return  new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<?> handleInsufficientBalanceException(InsufficientBalanceException ex)
    {
        Map<String, Object> error=new HashMap<>();
        error.put("timeStamp",LocalDateTime.now());
        error.put("error",ex.getMessage());
        return  new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }
}

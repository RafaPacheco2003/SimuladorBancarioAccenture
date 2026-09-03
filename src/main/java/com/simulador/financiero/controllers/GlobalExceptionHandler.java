package com.simulador.financiero.controllers;


import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.simulador.financiero.DTOs.response.ErrorDetail;
import com.simulador.financiero.Exceptions.DuplicateResourceException;
import com.simulador.financiero.Exceptions.InsufficientBalanceException;
import com.simulador.financiero.Exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetail> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail(LocalDateTime.now(), 404, "Not Found", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(404).body(errorDetail);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorDetail> handleInsufficentBalance(InsufficientBalanceException ex, HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail(LocalDateTime.now(), 422, "Insufficient Balance", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(422).body(errorDetail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetail> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail(LocalDateTime.now(), 400, "Bad Request", "Invalid input data", request.getRequestURI());
        return ResponseEntity.status(400).body(errorDetail);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorDetail> handleDuplicateResource(DuplicateResourceException ex, HttpServletRequest request){
        ErrorDetail errorDetail = new ErrorDetail(LocalDateTime.now(), 409, "Duplicate Resource", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(409).body(errorDetail);
    }
    

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetail> handleGenericException(Exception ex, HttpServletRequest request) {
        logger.error("An unexpected error occurred: ", ex);
        ErrorDetail errorDetail = new ErrorDetail(LocalDateTime.now(), 500, "Internal Server Error", "An unexpected error occurred", request.getRequestURI());
        return ResponseEntity.status(500).body(errorDetail);
    }


}

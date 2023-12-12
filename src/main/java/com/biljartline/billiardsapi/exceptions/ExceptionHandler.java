package com.biljartline.billiardsapi.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public BilliardsException handleException(Exception e) {
        log.error("An unhandled exception occurred", e);
        return new BilliardsException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {ResourceNotFoundException.class})
    public BilliardsException handleApiNotFoundException(ResourceNotFoundException e) {
        log.warn("A ResourceNotFoundException occurred", e);
        return new BilliardsException(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("An ArgumentNotValidException occurred", e);
        Map<String, String> errorMap = new HashMap<>();
        e.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidArgumentsException.class)
    public Map<String, String> handleApiInvalidArgumentsException(InvalidArgumentsException e) {
        log.warn("An ArgumentNotValidException occurred", e);
        Map<String, String> errorMap = new HashMap<>();
        e.getFieldErrors()
                .forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidArgumentException.class)
    public BilliardsException handleApiInvalidArgumentException(InvalidArgumentException e) {
        log.warn("An InvalidArgumentException occurred", e);
        return new BilliardsException(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(HttpMessageNotReadableException.class)
    public BilliardsException handleApiInvalidArgumentException(HttpMessageNotReadableException e) {
        log.warn("A HttpMessageNotReadableException occurred", e);
        return new BilliardsException(HttpStatus.BAD_REQUEST, e.getMessage());
    }

}

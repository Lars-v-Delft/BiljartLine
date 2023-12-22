package com.biljartline.billiardsapi.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.sasl.AuthenticationException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorMessage handleException(Exception e) {
        log.error("An unhandled exception occurred", e);
        return new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(AuthenticationException.class)
    public ErrorMessage handleAuthenticationException(AuthenticationException e) {
        log.error("An unhandled exception occurred", e);
        return new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, "User could not be authenticated");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ErrorMessage handleResourceNotFoundException(ResourceNotFoundException e) {
        log.warn("A ResourceNotFoundException occurred", e);
        return new ErrorMessage(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("An ArgumentNotValidException occurred", e);
        Map<String, String> errorMap = new HashMap<>();
        e.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidArgumentsException.class)
    public Map<String, String> handleApiInvalidArgumentsException(InvalidArgumentsException e) {
        log.warn("An ArgumentNotValidException occurred", e);
        Map<String, String> errorMap = new HashMap<>();
        e.getFieldErrors()
                .forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidArgumentException.class)
    public ErrorMessage handleInvalidArgumentException(InvalidArgumentException e) {
        log.warn("An InvalidArgumentException occurred", e);
        return new ErrorMessage(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorMessage handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("A HttpMessageNotReadableException occurred", e);
        return new ErrorMessage(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ErrorMessage handleBadCredentialsException(BadCredentialsException e) {
        log.warn("A BadCredentialsException occurred", e);
        return new ErrorMessage(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}

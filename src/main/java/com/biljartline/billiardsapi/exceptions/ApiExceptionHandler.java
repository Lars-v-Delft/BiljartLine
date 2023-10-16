package com.biljartline.billiardsapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {ApiNotFoundException.class})
    public ApiException handleNotFoundException(ApiNotFoundException e) {
        return new ApiException(HttpStatus.NOT_FOUND, e.getMessage());
    }
}

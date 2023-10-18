package com.biljartline.billiardsapi.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiException handleException(Exception e){
        log.error("An unhandled exception occurred", e);
        return new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {ApiNotFoundException.class})
    public ApiException handleNotFoundException(ApiNotFoundException e) {
        log.warn("A not found exception occurred", e);
        return new ApiException(HttpStatus.NOT_FOUND, e.getMessage());
    }
}

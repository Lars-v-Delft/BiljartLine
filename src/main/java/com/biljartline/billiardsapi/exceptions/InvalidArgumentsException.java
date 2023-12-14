package com.biljartline.billiardsapi.exceptions;

import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
public class InvalidArgumentsException extends RuntimeException{
    private final List<FieldError> fieldErrors;

    public InvalidArgumentsException(List<FieldError> fieldErrors){
        this.fieldErrors = fieldErrors;
    }
}

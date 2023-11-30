package com.biljartline.billiardsapi.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AfterValidator implements ConstraintValidator<After, String> {
    private String compareString;

    @Override
    public void initialize(After annotation) {
        compareString = annotation.compareDate();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        LocalDate compareDate;
        try {
            compareDate = LocalDate.parse(compareString);
        } catch (DateTimeParseException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate
                            ("compareDate must be of format yyyy-mm-dd")
                    .addConstraintViolation();
            return false;
        }

        LocalDate dateValue;
        try {
            dateValue = LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate
                            ("value must be of format yyyy-mm-dd")
                    .addConstraintViolation();
            return false;
        }

        return dateValue.isAfter(compareDate);
    }
}
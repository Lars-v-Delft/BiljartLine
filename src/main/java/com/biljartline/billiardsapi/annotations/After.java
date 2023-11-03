package com.biljartline.billiardsapi.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = AfterValidator.class)
public @interface After {
    String compareDate();
    String message() default "must be after: {compareDate}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

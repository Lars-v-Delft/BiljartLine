package com.biljartline.billiardsapi.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = BeforeValidator.class)
public @interface Before {
    String compareDate();
    String message() default "must be before: {compareDate}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

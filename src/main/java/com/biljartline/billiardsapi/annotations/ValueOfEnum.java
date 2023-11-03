package com.biljartline.billiardsapi.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ValueOfEnumValidator.class)
public @interface ValueOfEnum {
    Class<? extends Enum<?>> enumClass();
    String message() default "must be one of: {possibleValues}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

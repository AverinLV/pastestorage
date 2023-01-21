package com.example.pastestorage.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = AllowedValuesValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedValues {
    String message() default "Input value is not in list of allowed values: ";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String propName();
    String[] values();
}

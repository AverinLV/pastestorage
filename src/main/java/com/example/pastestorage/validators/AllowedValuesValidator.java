package com.example.pastestorage.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

public class AllowedValuesValidator implements ConstraintValidator<AllowedValues, Object> {

    private String propName;
    private String message;
    private Set<String> allowable;
    @Override
    public void initialize(AllowedValues constraintAnnotation) {
        this.propName = constraintAnnotation.propName();
        this.message = constraintAnnotation.message();
        this.allowable = Set.of(constraintAnnotation.values());
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Boolean valid = value == null || this.allowable.contains(value);
        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message.concat(this.allowable.toString()))
                    .addPropertyNode(this.propName).addConstraintViolation();
        }
        return valid;
    }
}

package com.example.pastestorage.validators;

import lombok.extern.log4j.Log4j2;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;
@Log4j2
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
            log.info(String.format("Validation failed. %s Doesn't match allowable variables: %s", value, allowable));
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(propName + " " + message.concat(this.allowable.toString()))
                    .addPropertyNode(this.propName).addConstraintViolation();
        }
        return valid;
    }
}

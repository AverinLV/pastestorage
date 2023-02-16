package com.example.pastestorage.validators;

import com.example.pastestorage.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {
    private final UserService userService;
    private String message;
    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Boolean valid = value == null || !userService.isUsernameExist(value);
        if (!valid) {
            log.info(String.format("Validation failed. %s username already exist in database", value));
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
        }
        return valid;
    }
}

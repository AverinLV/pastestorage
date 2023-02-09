package com.example.pastestorage.rest.advice;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.pastestorage.exceptions.PasteNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Component
public class ExceptionControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorInfo> handle(Exception exception) {
        ErrorInfo errorInfo = getSingleMessageErrorInfo(exception);
        exception.printStackTrace();
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorInfo> handle (JWTVerificationException exception) {
        ErrorInfo errorInfo = getSingleMessageErrorInfo(exception);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorInfo> handle (BadCredentialsException exception) {
        ErrorInfo errorInfo = getSingleMessageErrorInfo(exception);
        return new ResponseEntity<>(errorInfo, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorInfo> handle(PasteNotFoundException exception) {
        ErrorInfo errorInfo = getSingleMessageErrorInfo(exception);
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorInfo> handle(MethodArgumentNotValidException exception) {
        List<String> messages = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        Map<String, List<String>> exceptions = Collections.singletonMap(exception.getClass().getName(), messages);
        ErrorInfo errorInfo = new ErrorInfo(exceptions);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler
    public ResponseEntity<ErrorInfo> handle(ConstraintViolationException exception) {
        List<String> messages = exception.getConstraintViolations()
                .stream()
                .map(e -> e.getPropertyPath() + ": " + e.getMessage())
                .collect(Collectors.toList());
        Map<String, List<String>> exceptions = Collections.singletonMap(exception.getClass().getName(), messages);
        ErrorInfo errorInfo = new ErrorInfo(exceptions);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    private ErrorInfo getSingleMessageErrorInfo(Exception exception) {
        List<String> messages = Collections.singletonList(exception.getLocalizedMessage());
        Map<String, List<String>> exceptions = Collections.singletonMap(exception.getClass().getName(), messages);
        ErrorInfo errorInfo = new ErrorInfo(exceptions);
        return errorInfo;
    }

    @Getter
    @RequiredArgsConstructor
    private class ErrorInfo {
        private final Map<String, List<String>> exceptions;

    }
}
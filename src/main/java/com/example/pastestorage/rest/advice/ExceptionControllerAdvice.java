package com.example.pastestorage.rest.advice;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Component
public class ExceptionControllerAdvice {
    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<ErrorInfo> handle(Exception exception) {
        List<String> messages = Collections.singletonList(exception.getClass().getName() + ": " + exception.getLocalizedMessage());
        ErrorInfo errorInfo = new ErrorInfo(messages);
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<ErrorInfo> handle(MethodArgumentNotValidException exception) {
        exception.getBindingResult().getFieldErrors().stream().map(FieldError::getField).forEach(System.out::println);
        List<String> messages = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        ErrorInfo errorInfo = new ErrorInfo(messages);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<ErrorInfo> handle(ConstraintViolationException exception) {
        List<String> messages = exception.getConstraintViolations()
                .stream()
                .map(e -> e.getPropertyPath() + ": " + e.getMessage())
                .collect(Collectors.toList());
        ErrorInfo errorInfo = new ErrorInfo(messages);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @Getter
    @RequiredArgsConstructor
    private class ErrorInfo {
        private final List<String> messages;

    }


}
package com.example.pastestorage.rest.advice;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.pastestorage.dto.response.ErrorDTO;
import com.example.pastestorage.exceptions.PasteNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
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
    public ResponseEntity<ErrorDTO> handle(Exception exception) {
        ErrorDTO errorDTO = getSingleMessageErrorDTO(exception);
        exception.printStackTrace();
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handle (AccessDeniedException exception) {
        ErrorDTO errorDTO = getSingleMessageErrorDTO(exception);
        return new ResponseEntity<>(errorDTO, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handle (JWTVerificationException exception) {
        ErrorDTO errorDTO = getSingleMessageErrorDTO(exception);
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handle (BadCredentialsException exception) {
        ErrorDTO errorDTO = getSingleMessageErrorDTO(exception);
        return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handle(PasteNotFoundException exception) {
        ErrorDTO errorDTO = getSingleMessageErrorDTO(exception);
        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handle(MethodArgumentNotValidException exception) {
        List<String> messages = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        Map<String, List<String>> exceptions = Collections.singletonMap(exception.getClass().getName(), messages);
        ErrorDTO errorDTO = new ErrorDTO(exceptions);
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handle(ConstraintViolationException exception) {
        List<String> messages = exception.getConstraintViolations()
                .stream()
                .map(e -> e.getPropertyPath() + ": " + e.getMessage())
                .collect(Collectors.toList());
        Map<String, List<String>> exceptions = Collections.singletonMap(exception.getClass().getName(), messages);
        ErrorDTO errorDTO = new ErrorDTO(exceptions);
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    private ErrorDTO getSingleMessageErrorDTO(Exception exception) {
        List<String> messages = Collections.singletonList(exception.getLocalizedMessage());
        Map<String, List<String>> exceptions = Collections.singletonMap(exception.getClass().getName(), messages);
        ErrorDTO errorDTO = new ErrorDTO(exceptions);
        return errorDTO;
    }
    
}
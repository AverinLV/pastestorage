package com.example.pastestorage.rest.advice;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.pastestorage.dto.response.ErrorDTO;
import com.example.pastestorage.exceptions.PasteNotFoundException;
import com.example.pastestorage.exceptions.UnauthorizedActionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    @ExceptionHandler({UnauthorizedActionException.class, AccessDeniedException.class})
    public ResponseEntity<ErrorDTO> forbiddenError(Exception exception) {
        ErrorDTO errorDTO = getSingleMessageErrorDTO(exception);
        return new ResponseEntity<>(errorDTO, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({JWTVerificationException.class})
    public ResponseEntity<ErrorDTO> handleBadRequestOthers(Exception exception) {
        ErrorDTO errorDTO = getSingleMessageErrorDTO(exception);
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<ErrorDTO> handleUnauthorized(Exception exception) {
        ErrorDTO errorDTO = getSingleMessageErrorDTO(exception);
        return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({PasteNotFoundException.class, UsernameNotFoundException.class})
    public ResponseEntity<ErrorDTO> handleNotFound(Exception exception) {
        ErrorDTO errorDTO = getSingleMessageErrorDTO(exception);
        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handle(MethodArgumentNotValidException exception) {
        List<String> messages = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
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

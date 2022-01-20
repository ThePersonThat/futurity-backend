package com.alex.futurity.projectserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@ControllerAdvice
public class GlobalControllerHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        FieldError fieldError = result.getFieldError();
        ErrorMessage message = new ErrorMessage(fieldError.getDefaultMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ErrorMessage> handleConstraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> error = violations.stream().reduce((v, v2) -> v2).orElse(null);
        ErrorMessage message = new ErrorMessage(error.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler({ClientSideException.class})
    public ResponseEntity<ErrorMessage> handleClientSideException(ClientSideException e) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());

        return ResponseEntity.status(e.getStatus()).body(errorMessage);
    }
}

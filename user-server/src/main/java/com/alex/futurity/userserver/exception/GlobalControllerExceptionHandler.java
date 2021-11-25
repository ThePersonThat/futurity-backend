package com.alex.futurity.userserver.exception;

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
public class GlobalControllerExceptionHandler {

    @ExceptionHandler({UserAlreadyExistException.class})
    public ResponseEntity<ErrorMessage> handleUserAlreadyExistException(UserAlreadyExistException e) {
        ErrorMessage message = new ErrorMessage(e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<ErrorMessage> handleUserNotFoundException(UserNotFoundException e) {
        ErrorMessage message = new ErrorMessage(e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

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
}

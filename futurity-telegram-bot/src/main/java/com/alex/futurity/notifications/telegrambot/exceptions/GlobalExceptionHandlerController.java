package com.alex.futurity.notifications.telegrambot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler(NotificationException.class)
    public ResponseEntity<?> notificationException(NotificationException ex) {
        Map<String, String> dto = new HashMap<>();
        dto.put("message", ex.getMessage());
        dto.put("status", "400");
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }
}

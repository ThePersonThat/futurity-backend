package com.alex.futurity.authorizationserver.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ClientSideException extends RuntimeException {
    @Getter
    private final HttpStatus status;

    public ClientSideException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}

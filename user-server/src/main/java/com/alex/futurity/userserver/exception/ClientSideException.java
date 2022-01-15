package com.alex.futurity.userserver.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ClientSideException extends RuntimeException {
    private final HttpStatus status;

    public ClientSideException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}

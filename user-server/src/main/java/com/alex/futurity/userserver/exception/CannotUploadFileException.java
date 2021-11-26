package com.alex.futurity.userserver.exception;

public class CannotUploadFileException extends RuntimeException {
    public CannotUploadFileException(String message) {
        super(message);
    }
}

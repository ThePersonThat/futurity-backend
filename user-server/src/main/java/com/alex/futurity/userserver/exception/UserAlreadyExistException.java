package com.alex.futurity.userserver.exception;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException() {
        super("Error. A user with the same email address already exists");
    }
}

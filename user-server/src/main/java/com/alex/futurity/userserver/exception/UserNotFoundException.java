package com.alex.futurity.userserver.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("Mail or password is incorrect. Check the entered data");
    }
}

package com.alex.futurity.authorizationserver.exception;

public class WrongTokenCodeException extends RuntimeException {
    public WrongTokenCodeException() {
        super("Wrong code, check the code again");
    }
}

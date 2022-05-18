package com.alex.futurity.authorizationserver.exception;

public class OauthLoginFailedException extends RuntimeException {
    public OauthLoginFailedException() {
        super();
    }

    public OauthLoginFailedException(String message) {
        super(message);
    }

    public OauthLoginFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}

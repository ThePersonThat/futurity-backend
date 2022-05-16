package com.alex.futurity.authorizationserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    @ExceptionHandler({HttpClientErrorException.class})
    public ResponseEntity<String> handleHttpClientErrorException(HttpClientErrorException e) {
        return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        FieldError fieldError = result.getFieldError();
        ErrorMessage message = new ErrorMessage(fieldError.getDefaultMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler({MultipartException.class})
    public ResponseEntity<ErrorMessage> handleFileSizeLimitExceededException() {
        ErrorMessage errorMessage = new ErrorMessage("Avatar is too large. Max size 5MB");

        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(errorMessage);
    }

    @ExceptionHandler({IllegalStateException.class})
    public ResponseEntity<ErrorMessage> handleIllegalStateException(IllegalStateException e) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }

    @ExceptionHandler({MailSendException.class})
    public ResponseEntity<ErrorMessage> handleMailSendException() {
        ErrorMessage errorMessage = new ErrorMessage("Failed to send email. Try again after a while");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }

    @ExceptionHandler({ClientSideException.class})
    public ResponseEntity<ErrorMessage> handleClientSideException(ClientSideException e) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(errorMessage);
    }

    @ExceptionHandler(OauthLoginFailedException.class)
    public ResponseEntity<ErrorMessage> handleOauthLoginFailedException(OauthLoginFailedException e) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }
}

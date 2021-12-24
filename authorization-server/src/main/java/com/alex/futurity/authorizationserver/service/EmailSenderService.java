package com.alex.futurity.authorizationserver.service;

public interface EmailSenderService {
    void sendConfirmationMessage(String email, String code);
}

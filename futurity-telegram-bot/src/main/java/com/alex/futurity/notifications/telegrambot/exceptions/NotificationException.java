package com.alex.futurity.notifications.telegrambot.exceptions;

public class NotificationException extends RuntimeException {
    public NotificationException() {
        super();
    }

    public NotificationException(String message) {
        super(message);
    }

    public NotificationException(String message, Throwable cause) {
        super(message, cause);
    }
}

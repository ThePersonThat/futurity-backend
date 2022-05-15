package com.alex.futurity.notifications.telegrambot.service.notitifcation;

import com.alex.futurity.notifications.telegrambot.entity.TelegramUser;
import com.alex.futurity.notifications.telegrambot.exceptions.NotificationException;

public interface NotificationManager {

    void sendNotification(String text, String userId) throws NotificationException;

    void addUserToNotificationList(TelegramUser telegramUser) throws Exception;

    void removeUserFromNotificationList(Long id) throws NotificationException;

    void enableNotification(String chatId) throws NotificationException;

    void disableNotification(String chatId) throws NotificationException;
}

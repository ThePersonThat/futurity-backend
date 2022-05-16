package com.alex.futurity.notifications.telegrambot.service.notitifcation;

import com.alex.futurity.notifications.telegrambot.entity.TelegramUser;
import com.alex.futurity.notifications.telegrambot.exceptions.NotificationException;
import com.alex.futurity.notifications.telegrambot.repository.TelegramUserRepository;
import com.alex.futurity.notifications.telegrambot.service.bot.send.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultNotificationManager implements NotificationManager {
    private final MessageSender messageSender;
    private final TelegramUserRepository userRepository;

    @Autowired
    public DefaultNotificationManager(MessageSender messageSender, TelegramUserRepository userRepository) {
        this.messageSender = messageSender;
        this.userRepository = userRepository;
    }

    @Override
    public void sendNotification(String text, String userId) throws NotificationException {
        TelegramUser user = userRepository.findTelegramUserByUserId(userId);
        if (user == null)
            throw new NotificationException("Notification sent failed. Reason: user not exist");
        try {
            this.messageSender.sendMessage(text, user.getTelegramChatId());
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new NotificationException("Notification sent failed. Reason: api failed. ERROR CODE: 500");
        }
    }

    @Override
    public void sendNotificationByChatId(String text, String chatId) throws NotificationException {
        try {
            this.messageSender.sendMessage(text, chatId);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new NotificationException("API ERROR. STATUS CODE: 500");
        }
    }

    @Override
    @Transactional
    public void addUserToNotificationList(TelegramUser telegramUser) {
        this.userRepository.save(telegramUser);
    }

    @Override
    @Transactional
    public void removeUserFromNotificationList(Long id) {
        this.userRepository.deleteById(id);
    }

    @Override
    public void enableNotification(String chatId) {
        TelegramUser user = this.userRepository.findTelegramUserByTelegramChatId(chatId);
        if (user == null) {
            throw new NotificationException("Oops, we have some troubles...");
        }
        if(user.isEnabledNotifications()) {
            user.setEnabledNotifications(true);
            this.userRepository.save(user);
        }
    }

    @Override
    public void disableNotification(String chatId) {
        TelegramUser user = this.userRepository.findTelegramUserByTelegramChatId(chatId);
        if (user == null) {
            throw new NotificationException("Oops, we have some troubles...");
        }
        if(user.isEnabledNotifications()) {
            user.setEnabledNotifications(false);
            this.userRepository.save(user);
        }
    }
}

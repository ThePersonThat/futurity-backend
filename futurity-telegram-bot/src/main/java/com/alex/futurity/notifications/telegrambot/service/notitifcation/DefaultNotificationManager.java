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
    @Transactional
    public void addUserToNotificationList(TelegramUser telegramUser) {
        this.userRepository.save(telegramUser);
    }

    @Override
    @Transactional
    public void removeUserFromNotificationList(Long id) {
        this.userRepository.deleteById(id);
    }
}

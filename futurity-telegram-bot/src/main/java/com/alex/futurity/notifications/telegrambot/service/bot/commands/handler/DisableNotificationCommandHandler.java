package com.alex.futurity.notifications.telegrambot.service.bot.commands.handler;

import com.alex.futurity.notifications.telegrambot.exceptions.NotificationException;
import com.alex.futurity.notifications.telegrambot.service.bot.send.MessageSender;
import com.alex.futurity.notifications.telegrambot.service.notitifcation.NotificationManager;
import com.alex.futurity.notifications.telegrambot.service.util.BotUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class DisableNotificationCommandHandler implements CommandHandler {
    private final MessageSender messageSender;
    private final NotificationManager notificationManager;

    @Autowired
    public DisableNotificationCommandHandler(MessageSender messageSender, NotificationManager notificationManager) {
        this.messageSender = messageSender;
        this.notificationManager = notificationManager;
    }

    @Override
    public void handleCommand(Update update) throws Exception {
        String chatId = BotUtils.getChatId(update);
        try {
            this.notificationManager.enableNotification(chatId);
            this.messageSender.sendMessage("We are successful remove you from mailing list!", chatId);
        } catch (NotificationException e) {
            this.messageSender.sendMessage("We can't disable your notifications. Sorry!", chatId);
        }
    }
}

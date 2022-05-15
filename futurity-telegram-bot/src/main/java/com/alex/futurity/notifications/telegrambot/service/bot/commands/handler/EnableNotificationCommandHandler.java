package com.alex.futurity.notifications.telegrambot.service.bot.commands.handler;

import com.alex.futurity.notifications.telegrambot.exceptions.NotificationException;
import com.alex.futurity.notifications.telegrambot.repository.TelegramUserRepository;
import com.alex.futurity.notifications.telegrambot.service.bot.send.MessageSender;
import com.alex.futurity.notifications.telegrambot.service.notitifcation.NotificationManager;
import com.alex.futurity.notifications.telegrambot.service.util.BotUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class EnableNotificationCommandHandler implements CommandHandler {
    private final MessageSender messageSender;
    private final TelegramUserRepository userRepository;
    private final NotificationManager notificationManager;

    @Autowired
    public EnableNotificationCommandHandler(MessageSender messageSender, TelegramUserRepository userRepository, NotificationManager notificationManager) {
        this.messageSender = messageSender;
        this.userRepository = userRepository;
        this.notificationManager = notificationManager;
    }

    @Override
    public void handleCommand(Update update) throws Exception {
        String chatId = BotUtils.getChatId(update);
        try {
            this.notificationManager.enableNotification(chatId);
            this.messageSender.sendMessage("We are successful added you to mailing list!", chatId);
        } catch(NotificationException e) {
            this.messageSender.sendMessage("We can't enable your notifications. Sorry:(", chatId);
        }

    }
}

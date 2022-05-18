package com.alex.futurity.notifications.telegrambot.service.bot.commands.handler;

import com.alex.futurity.notifications.telegrambot.entity.TelegramUser;
import com.alex.futurity.notifications.telegrambot.repository.TelegramUserRepository;
import com.alex.futurity.notifications.telegrambot.service.bot.send.MessageSender;
import com.alex.futurity.notifications.telegrambot.service.notitifcation.NotificationManager;
import com.alex.futurity.notifications.telegrambot.service.util.BotUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class StartCommandHandler implements CommandHandler {
    private final MessageSender sender;
    private final TelegramUserRepository userRepository;
    private final NotificationManager notificationManager;
    @Value("${app.telegram.bot.link.auth}")
    private String authLink;

    @Autowired
    public StartCommandHandler(MessageSender sender, TelegramUserRepository userRepository, NotificationManager notificationManager) {
        this.sender = sender;
        this.userRepository = userRepository;
        this.notificationManager = notificationManager;
    }

    @Override
    public void handleCommand(Update update) throws Exception {
        String chatId = BotUtils.getChatId(update);
        this.sender.sendMessage("Welcome to futurity bot! This bot will send notifications for you.", chatId);
        TelegramUser user = this.userRepository.findTelegramUserByTelegramChatId(chatId);
        if (user != null) {
            this.sender.sendMessage("Hello, " + user.getUsername() + ". We are remember you! Your id: " + user.getUserId(), chatId);
        } else if (BotUtils.isNeedToSplit(update)) {
            String[] commandArgs = BotUtils.splitCommand(update);
            String userId = commandArgs[1];
            TelegramUser telegramUser = this.buildUserFromUpdate(update, userId);
            this.notificationManager.addUserToNotificationList(telegramUser);
            this.sender.sendMessage("Congratulations! We are success added you to mailing list!", chatId);
        } else {
            SendMessage sendMessage = new SendMessage();
            sendMessage.enableMarkdown(true);
            sendMessage.setChatId(chatId);
            String authLink = loginUserUrl(chatId);
            sendMessage.setText(String.format("Please, authorize in our bot. Please, follow this: %s", authLink));
            this.sender.sendMessage(sendMessage);
        }
    }

    private TelegramUser buildUserFromUpdate(Update update, String userId) {
        return TelegramUser.builder()
                .telegramChatId(BotUtils.getChatId(update))
                .enabledNotifications(true)
                .userId(userId)
                .username(BotUtils.getUsername(update))
                .build();
    }

    private String loginUserUrl(String chatId) {
        return authLink + "?id=" + chatId;
    }
}

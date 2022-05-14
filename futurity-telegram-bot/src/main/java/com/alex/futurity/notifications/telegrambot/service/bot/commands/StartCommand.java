package com.alex.futurity.notifications.telegrambot.service.bot.commands;

import com.alex.futurity.notifications.telegrambot.entity.TelegramUser;
import com.alex.futurity.notifications.telegrambot.service.bot.send.MessageSender;
import com.alex.futurity.notifications.telegrambot.service.notitifcation.NotificationManager;
import com.alex.futurity.notifications.telegrambot.service.util.BotUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class StartCommand implements Command {
    @Value("${app.telegram.bot.link.auth}")
    private String authLink;
    private final MessageSender sender;
    private final NotificationManager notificationManager;

    @Autowired
    public StartCommand(MessageSender sender, NotificationManager notificationManager) {
        this.sender = sender;
        this.notificationManager = notificationManager;
    }


    @Override
    public void execute(Update update) throws Exception {
        String chatId = BotUtils.getChatId(update);
        this.sender.sendMessage("Welcome to futurity bot! This bot will send notifications for you.", chatId);
        if(BotUtils.isNeedToSplit(update)) {
            String[] commandArgs = BotUtils.splitCommand(update);
            String userId = commandArgs[1];
            TelegramUser telegramUser = this.buildUserFromUpdate(update, userId);
            this.notificationManager.addUserToNotificationList(telegramUser);
            this.sender.sendMessage("Congratulations! We are success added you to mailing list!", chatId);
        } else {
            SendMessage sendMessage = new SendMessage();
            sendMessage.enableMarkdown(true);
            sendMessage.setChatId(chatId);
            sendMessage.setText(String.format("Please, authorize in our bot. Please, follow this: [link](%s)", authLink));
            this.sender.sendMessage(sendMessage);
        }
    }

    @Override
    public CommandName getCommandName() {
        return CommandName.START;
    }
//todo make chat id unique
    private TelegramUser buildUserFromUpdate(Update update, String userId) {
        return TelegramUser.builder()
                .telegramChatId(BotUtils.getChatId(update))
                .enabledNotifications(true)
                .userId(userId)
                .username(BotUtils.getUsername(update))
                .build();
    }
}

package com.alex.futurity.notifications.telegrambot.service.bot.commands;

import com.alex.futurity.notifications.telegrambot.entity.TelegramUser;
import com.alex.futurity.notifications.telegrambot.exceptions.UserNotFoundException;
import com.alex.futurity.notifications.telegrambot.repository.TelegramUserRepository;
import com.alex.futurity.notifications.telegrambot.service.bot.send.MessageSender;
import com.alex.futurity.notifications.telegrambot.service.util.BotUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class DisableNotificationCommand implements Command {
    private final TelegramUserRepository telegramUserRepository;
    private final MessageSender messageSender;

    @Autowired
    public DisableNotificationCommand(TelegramUserRepository telegramUserRepository, MessageSender messageSender) {
        this.telegramUserRepository = telegramUserRepository;
        this.messageSender = messageSender;
    }

    @Override
    public void execute(Update update) throws Exception {
        String chatId = BotUtils.getChatId(update);
        TelegramUser telegramUser = this.telegramUserRepository.findTelegramUserByTelegramChatId(chatId);
        if (telegramUser == null)
            throw new UserNotFoundException("We can't find user with this chat id: " + chatId);
        telegramUser.setEnabledNotifications(false);
        this.telegramUserRepository.save(telegramUser);
        this.messageSender.sendMessage("We are successful remove you from mailing list!", chatId);
    }

    @Override
    public CommandName getCommandName() {
        return CommandName.DISABLE_NOTIFICATION;
    }
}

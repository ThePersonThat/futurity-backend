package com.alex.futurity.notifications.telegrambot.service.bot.send;

import com.alex.futurity.notifications.telegrambot.service.bot.domain.FuturityBot;
import com.alex.futurity.notifications.telegrambot.service.util.BotUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class DefaultTelegramBotMessageSender implements MessageSender {
    private final FuturityBot bot;

    @Autowired
    public DefaultTelegramBotMessageSender(FuturityBot bot) {
        this.bot = bot;
    }

    @Override
    public void sendMessage(String text, String chatId) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage(chatId, text);
        this.bot.execute(sendMessage);
    }

    @Override
    public void sendMessage(SendMessage message) throws TelegramApiException {
        this.bot.execute(message);
//        new SendSticker(message.getChatId(), new InputFile().set)
//        this.bot.execute()
    }
}

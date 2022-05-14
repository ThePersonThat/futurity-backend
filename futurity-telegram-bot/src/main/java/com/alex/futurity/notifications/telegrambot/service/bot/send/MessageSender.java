package com.alex.futurity.notifications.telegrambot.service.bot.send;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface MessageSender {

    void sendMessage(String text, String to) throws Exception;

    void sendMessage(SendMessage message) throws Exception;
}

package com.alex.futurity.notifications.telegrambot.service.bot.commands.handler;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandHandler {

    void handleCommand(Update update) throws Exception;

}

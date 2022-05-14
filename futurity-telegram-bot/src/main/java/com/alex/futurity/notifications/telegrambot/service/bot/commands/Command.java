package com.alex.futurity.notifications.telegrambot.service.bot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {

    void execute(Update update) throws Exception;

    CommandName getCommandName();

    @Autowired
    default void registerInContainer(CommandContainer commandContainer) {
        commandContainer.addCommand(getCommandName().getCommandName(), this);
    }
}

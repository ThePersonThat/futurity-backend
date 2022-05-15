package com.alex.futurity.notifications.telegrambot.service.bot.commands;

import com.alex.futurity.notifications.telegrambot.service.bot.commands.handler.CommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class StartCommand implements Command {
    private final CommandHandler commandHandler;

    @Autowired
    public StartCommand(@Qualifier("startCommandHandler") CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }


    @Override
    public void execute(Update update) throws Exception {
        this.commandHandler.handleCommand(update);
    }

    @Override
    public CommandName getCommandName() {
        return CommandName.START;
    }
}

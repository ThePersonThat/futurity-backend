package com.alex.futurity.notifications.telegrambot.service.bot.commands;

import com.alex.futurity.notifications.telegrambot.service.bot.send.MessageSender;
import com.alex.futurity.notifications.telegrambot.service.util.BotUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UnknownCommand implements Command {
    private final String DEFAULT_MESSAGE = "Sorry, we are not found this command. /help - for list all commands";
    private final MessageSender sender;
    public UnknownCommand(MessageSender sender) {
        this.sender = sender;
    }
    @Override
    public void execute(Update update) throws Exception {
        this.sender.sendMessage(DEFAULT_MESSAGE, BotUtils.getChatId(update));
    }

    @Override
    public CommandName getCommandName() {
        return CommandName.UNKNOWN;
    }
}

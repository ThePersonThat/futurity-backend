package com.alex.futurity.notifications.telegrambot.service.bot.commands;

import com.alex.futurity.notifications.telegrambot.service.bot.send.MessageSender;
import com.alex.futurity.notifications.telegrambot.service.util.BotUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class HelpCommand implements Command {
    private final MessageSender messageSender;
    private final static String DEFAULT_HELP_MESSAGE_TEXT =
            new StringBuilder()
                    .append("/enableNotification - enable your notifications and sends it when deadline is coming")
                    .append("\n")
                    .append("/disableNotification - disable all your notifications").toString();

    public HelpCommand(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Override
    public void execute(Update update) throws Exception {
        String chatId = BotUtils.getChatId(update);
        this.messageSender.sendMessage(DEFAULT_HELP_MESSAGE_TEXT, chatId);
    }

    @Override
    public CommandName getCommandName() {
        return CommandName.HELP;
    }
}

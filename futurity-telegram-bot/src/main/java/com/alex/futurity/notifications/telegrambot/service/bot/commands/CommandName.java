package com.alex.futurity.notifications.telegrambot.service.bot.commands;

public enum CommandName {
    START("/start"),
    UNKNOWN("/unknown"),
    HELP("/help"),
    ENABLE_NOTIFICATION("/enableNotification"),
    DISABLE_NOTIFICATION("/disableNotification");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}

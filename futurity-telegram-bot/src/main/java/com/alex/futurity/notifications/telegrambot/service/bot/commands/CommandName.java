package com.alex.futurity.notifications.telegrambot.service.bot.commands;

public enum CommandName {
    START("/start"),
    UNKNOWN("/unknown"),
    HELP("/help");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}

package com.alex.futurity.notifications.telegrambot.service.bot.commands;

import com.alex.futurity.notifications.telegrambot.service.bot.send.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;



public class CommandContainer {
    private final Map<String, Command> commandMap = new HashMap<>();
    private final UnknownCommand unknownCommand;

    public CommandContainer(MessageSender sender) {
        this.unknownCommand = new UnknownCommand(sender);
    }

    public void addCommand(String commandName,Command command) {
        this.commandMap.put(commandName, command);
    }
    public Command getCommand(String commandName) {
        return this.commandMap.getOrDefault(commandName, unknownCommand);
    }
}

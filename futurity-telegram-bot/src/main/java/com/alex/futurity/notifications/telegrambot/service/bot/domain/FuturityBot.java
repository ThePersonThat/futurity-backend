package com.alex.futurity.notifications.telegrambot.service.bot.domain;

import com.alex.futurity.notifications.telegrambot.service.bot.commands.Command;
import com.alex.futurity.notifications.telegrambot.service.bot.commands.CommandContainer;
import com.alex.futurity.notifications.telegrambot.service.bot.commands.StartCommand;
import com.alex.futurity.notifications.telegrambot.service.bot.send.DefaultTelegramBotMessageSender;
import com.alex.futurity.notifications.telegrambot.service.util.BotUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@Configuration
public class FuturityBot extends TelegramLongPollingBot {
    @Value("${app.telegram.bot.username}")
    private String botUsername;
    @Value("${app.telegram.bot.token}")
    private String botToken;
    private final Logger logger = LoggerFactory.getLogger(FuturityBot.class);
    @Autowired
    @Lazy
    private CommandContainer commandContainer;

    public FuturityBot() {
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        this.logger.info("Received a new message: {}", update.getMessage().getText());
        String commandName = BotUtils.splitCommand(update)[0];
        Command command = this.commandContainer.getCommand(commandName);
        try {
            command.execute(update);
            this.execute(new SendSticker(BotUtils.getChatId(update), new InputFile("CAACAgIAAxkBAANjYn_S3p2mWpH_t1jfpX40bGP-Hw4AAiEUAAIh78lL3T7pTgIL7zEkBA")));
        } catch (Exception exception) {
            exception.printStackTrace();
            SendMessage sendMessage = new SendMessage(BotUtils.getChatId(update), "Oops. We have some troubles, try again later. Sorry!");
            try {
                this.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Bean
    public CommandContainer commandContainer() {
        return new CommandContainer(new DefaultTelegramBotMessageSender(this));
    }
}

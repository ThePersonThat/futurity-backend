package com.alex.futurity.notifications.telegrambot.service.util;

import org.telegram.telegrambots.meta.api.objects.Update;

public class BotUtils {

    public static String getChatId(Update update) {
        return String.valueOf(update.getMessage().getChatId());
    }

    public static String getText(Update update) {
        return update.getMessage().getText();
    }

    public static String getUsername(Update update) {
        return update.getMessage().getFrom().getUserName();
    }

    public static String[] splitCommand(Update update) {
        return BotUtils.getText(update).split(" ");
    }

    public static boolean isNeedToSplit(Update update) {
        return splitCommand(update).length != 1;
    }
}

package com.alex.futurity.notifications.telegrambot.service.bot.login;

public interface OauthSuccessEventHandler {

    void handleSuccessLogin(String userId, String chatId, String username);

}

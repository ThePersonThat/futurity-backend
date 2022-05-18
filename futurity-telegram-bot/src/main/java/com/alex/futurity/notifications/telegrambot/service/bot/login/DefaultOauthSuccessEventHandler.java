package com.alex.futurity.notifications.telegrambot.service.bot.login;

import com.alex.futurity.notifications.telegrambot.entity.TelegramUser;
import com.alex.futurity.notifications.telegrambot.repository.TelegramUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultOauthSuccessEventHandler implements OauthSuccessEventHandler {
    private final TelegramUserRepository telegramUserRepository;

    @Override
    public void handleSuccessLogin(String userId, String chatId, String username) {
        TelegramUser telegramUser = TelegramUser.builder()
                .telegramChatId(chatId)
                .userId(userId)
                .enabledNotifications(true)
                .username(username)
                .build();
        this.telegramUserRepository.save(telegramUser);
    }
}

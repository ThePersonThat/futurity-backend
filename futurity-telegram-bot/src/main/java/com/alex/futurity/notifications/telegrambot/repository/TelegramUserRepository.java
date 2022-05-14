package com.alex.futurity.notifications.telegrambot.repository;

import com.alex.futurity.notifications.telegrambot.entity.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {

    TelegramUser findTelegramUserByTelegramChatId(String chatId);

    TelegramUser findTelegramUserByUserId(String userId);

}

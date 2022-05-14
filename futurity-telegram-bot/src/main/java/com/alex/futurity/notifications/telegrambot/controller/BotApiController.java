package com.alex.futurity.notifications.telegrambot.controller;

import com.alex.futurity.notifications.telegrambot.dto.NotificationMessageDTO;
import com.alex.futurity.notifications.telegrambot.service.bot.commands.CommandContainer;
import com.alex.futurity.notifications.telegrambot.service.notitifcation.NotificationManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BotApiController {
    private final NotificationManager notificationManager;

    public BotApiController(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    @GetMapping("/telegram/send/")
    public ResponseEntity<?> sendTelegram(@RequestBody NotificationMessageDTO message) {
        this.notificationManager.sendNotification(message.getMessage(), message.getUserId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

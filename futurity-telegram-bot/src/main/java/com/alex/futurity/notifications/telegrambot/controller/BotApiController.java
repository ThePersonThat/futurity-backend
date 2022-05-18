package com.alex.futurity.notifications.telegrambot.controller;

import com.alex.futurity.notifications.telegrambot.dto.NotificationMessageDTO;
import com.alex.futurity.notifications.telegrambot.service.bot.login.OauthSuccessEventHandler;
import com.alex.futurity.notifications.telegrambot.service.notitifcation.NotificationManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@Log4j2
@RequiredArgsConstructor
public class BotApiController {
    private final NotificationManager notificationManager;
    private final OauthSuccessEventHandler oauthSuccessEventHandler;

    @GetMapping("/telegram/send/")
    public ResponseEntity<?> sendTelegram(@RequestBody NotificationMessageDTO message) {
        this.notificationManager.sendNotification(message.getMessage(), message.getUserId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/oauth/success")
    public ResponseEntity<?> oauthLoginSuccess(HttpServletRequest request) throws IOException {
        String chatId = request.getParameter("id");
        String userId = request.getParameter("userId");
        String username = request.getParameter("username");
        this.oauthSuccessEventHandler.handleSuccessLogin(userId, chatId, username);
        this.notificationManager.sendNotificationByChatId("We authorize you, " + username + ". Your id is: " + userId, chatId);
        return ResponseEntity.ok().build();
    }
}

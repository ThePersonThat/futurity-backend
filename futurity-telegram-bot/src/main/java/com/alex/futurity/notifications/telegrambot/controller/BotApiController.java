package com.alex.futurity.notifications.telegrambot.controller;

import com.alex.futurity.notifications.telegrambot.dto.NotificationMessageDTO;
import com.alex.futurity.notifications.telegrambot.service.notitifcation.NotificationManager;
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

    @GetMapping("/oauth/success")
    public ResponseEntity<?> oauthLoginSuccess(HttpServletRequest request) throws IOException {
        log.info(request.getParameter("accessToken"));
        log.info(request.getParameter("refreshToken"));
        String id = request.getParameter("id");
        log.info(id);
        this.notificationManager.sendNotificationByChatId("We authorize you", id);
        return ResponseEntity.ok().build();
    }
}

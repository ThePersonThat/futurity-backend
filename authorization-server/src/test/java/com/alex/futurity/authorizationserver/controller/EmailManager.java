package com.alex.futurity.authorizationserver.controller;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@Component
public class EmailManager {
    @Value("${spring.mail.username}")
    private String login;

    @Getter
    @Value("${spring.mail.password}")
    private String password = "alexRoot";

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.host}")
    private String host;
    @Getter
    private String email = "alex@gmail.com";
    private GreenMail mail;

    @SneakyThrows
    public void clearMessages() {
        mail.purgeEmailFromAllMailboxes();
    }

    @SneakyThrows
    public Message checkCountMessagesAndGet(int count, int index) {
        MimeMessage[] messages = mail.getReceivedMessagesForDomain(email);
        assertThat(messages).hasSize(count);
        MimeMessage message = messages[index];
        String content = getContentFromMessage(message);

        return new Message(content, message.getSubject());
    }

    public void checkCountMessages(int count) {
        MimeMessage[] messages = mail.getReceivedMessagesForDomain(email);
        assertThat(messages).hasSize(count);
    }

    private String getContentFromMessage(MimeMessage message) throws MessagingException, IOException {
        MimeMultipart body = (MimeMultipart) message.getContent();

        return  getContentFromMessage(body);
    }

    private String getContentFromMessage(MimeMultipart body) throws MessagingException, IOException {
        StringBuilder content = new StringBuilder();

        for (int i = 0; i < body.getCount(); i++) {
            BodyPart part = body.getBodyPart(i);
            Object bodyPart = part.getContent();

            if (bodyPart instanceof MimeMultipart) {
                String html = getContentFromMessage((MimeMultipart) bodyPart);
                content.append(html);
            } else {
                content.append((String) bodyPart);
            }
        }

        return content.toString();
    }

    @PostConstruct
    private void start() {
        mail = new GreenMail(new ServerSetup(port, host, ServerSetup.PROTOCOL_SMTP));
        mail.setUser(email, login, password);
        mail.start();
    }

    @PreDestroy
    private void destroy() {
        mail.stop();
    }
}

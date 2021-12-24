package com.alex.futurity.authorizationserver.service.impl;

import com.alex.futurity.authorizationserver.service.EmailSenderService;
import com.alex.futurity.authorizationserver.utils.html.HtmlGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
@Log4j2
public class EmailSenderServiceImpl implements EmailSenderService {
    private final JavaMailSender mailSender;
    private final HtmlGenerator generator;

    @Override
    @Async
    public void sendConfirmationMessage(String email, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setText(generator.generateHtml(code), true);
            helper.setSubject("Confirm your email");

            mailSender.send(message);
            log.info("The confirmation message is send to the {}", email);
        } catch (MessagingException e) {
            log.error("Failed to send email: ", e);
            throw new MailSendException("Failed to send email. Try again after a while");
        }
    }
}

package com.alex.futurity.authorizationserver.service;

import com.alex.futurity.authorizationserver.properties.EmailProperties;
import com.alex.futurity.authorizationserver.utils.html.HtmlGenerator;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@EnableAsync
public class EmailSenderService {
    private final HtmlGenerator generator;
    private final OAuth2Service oAuth2Service;
    private final EmailProperties emailProperties;

    public EmailSenderService(@Qualifier("confirmationHtmlGenerator") HtmlGenerator generator,
                              OAuth2Service oAuth2Service,
                              EmailProperties emailProperties) {
        this.generator = generator;
        this.oAuth2Service = oAuth2Service;
        this.emailProperties = emailProperties;
    }

    @Async
    public void sendConfirmationMessage(String email, String code) {
        if (emailProperties.isSendingEnabled()) {
            sendEmail(email, code);
        }
    }

    private void sendEmail(String email, String code) {
        try {
            Transport.send(buildMessage(email, code), emailProperties.getUsername(), oAuth2Service.getAccessToken());
            log.info("The confirmation message is send to the {}", email);
        } catch (MessagingException e) {
            log.error("Failed to send email: ", e);
            throw new MailSendException("Failed to send email. Try again after a while");
        }
    }

    private MimeMessage buildMessage(String email, String code) throws MessagingException {
        Session session = Session.getInstance(emailProperties.getEmailProperties());
        MimeMessage message = new MimeMessage(session);

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setText(generator.generateHtml(code), true);
        helper.setSubject("Confirm your email");

        return message;
    }
}

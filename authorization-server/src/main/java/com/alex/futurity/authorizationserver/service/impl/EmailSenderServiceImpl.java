package com.alex.futurity.authorizationserver.service.impl;

import com.alex.futurity.authorizationserver.service.EmailSenderService;
import com.alex.futurity.authorizationserver.service.OAuth2Service;
import com.alex.futurity.authorizationserver.utils.html.HtmlGenerator;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Properties;

@Service
@Log4j2
@EnableAsync
@ConfigurationProperties(prefix = "spring.mail")
public class EmailSenderServiceImpl implements EmailSenderService {
    private final HtmlGenerator generator;
    private final OAuth2Service oAuth2Service;
    private Map<String, String> properties;
    @Value("${mail.enable}")
    private boolean emailSendingEnabled;


    public EmailSenderServiceImpl(@Qualifier("confirmationHtmlGenerator") HtmlGenerator generator, OAuth2Service oAuth2Service) {
        this.generator = generator;
        this.oAuth2Service = oAuth2Service;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    @Async
    public void sendConfirmationMessage(String email, String code) {
        if (emailSendingEnabled) {
            try {
                Session session = setupSession();
                MimeMessage message = new MimeMessage(session);
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(email);
                helper.setText(generator.generateHtml(code), true);
                helper.setSubject("Confirm your email");

                Transport.send(message, oAuth2Service.getUsername(), oAuth2Service.getAccessToken());
                log.info("The confirmation message is send to the {}", email);
            } catch (MessagingException e) {
                log.error("Failed to send email: ", e);
                throw new MailSendException("Failed to send email. Try again after a while");
            }
        }
    }

    private Session setupSession() throws MessagingException {
        Properties props = new Properties();
        props.putAll(properties);

        return Session.getInstance(props);
    }
}

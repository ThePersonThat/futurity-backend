package com.alex.futurity.authorizationserver.service.impl;

import com.alex.futurity.authorizationserver.utils.html.HtmlGenerator;
import lombok.SneakyThrows;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailSenderServiceImplTest {
    @Mock
    private HtmlGenerator generator;
    @Mock
    private JavaMailSender sender;

    @InjectMocks
    private EmailSenderServiceImpl service;
    private static LogCaptor captor;
    private final String email = "alex@jpeg.com";
    private final String code = "123456";

    @BeforeAll
    private static void setCaptor() {
        captor = LogCaptor.forClass(EmailSenderServiceImpl.class);
    }

    @BeforeEach
    private void clearLogs() {
        captor.clearLogs();
    }

    @AfterAll
    private static void closeCaptor() {
        captor.close();
    }

    @Test
    @DisplayName("Should send confirmation message email")
    @SneakyThrows
    void testSendConfirmationMessage() {

        String html = "<html></html>";
        MimeMessage mockMessage = mock(MimeMessage.class);

        try (MockedConstruction<MimeMessageHelper> mockHelper = mockConstruction(MimeMessageHelper.class)) {
            when(generator.generateHtml(anyString())).thenReturn(html);
            when(sender.createMimeMessage()).thenReturn(mockMessage);

            service.sendConfirmationMessage(email, code);

            MimeMessageHelper helper = mockHelper.constructed().get(0);
            verify(helper).setTo(eq(email));
            verify(generator).generateHtml(eq(code));
            verify(helper).setText(eq(html), eq(true));
            verify(helper).setSubject(eq("Confirm your email"));
            verify(sender).send(eq(mockMessage));
            assertThat(captor.getLogs())
                    .hasSize(1)
                    .contains(String.format("The confirmation message is send to the %s", email));
        }
    }

    @Test
    @DisplayName("Should throw an MailSendException if email is not sent")
    void testSendConfirmationMessageWithException() {
        String exceptionMessage = "Error setting setTO";

        try (MockedConstruction<MimeMessageHelper> mockHelper = mockConstruction(MimeMessageHelper.class, ((mock, context) ->
                doThrow(new MessagingException(exceptionMessage)).when(mock).setTo(anyString())))) {

            assertThatThrownBy(() -> service.sendConfirmationMessage(email, code))
                    .isInstanceOf(MailSendException.class)
                    .hasMessage("Failed to send email. Try again after a while");

            assertThat(captor.getLogs())
                    .hasSize(1)
                    .startsWith("Failed to send email: ");
        }
    }
}
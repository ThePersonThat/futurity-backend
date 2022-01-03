package com.alex.futurity.authorizationserver.controller;

import com.alex.futurity.authorizationserver.dto.ConfirmEmailRequestDTO;
import com.alex.futurity.authorizationserver.dto.LoginRequestDTO;
import com.alex.futurity.authorizationserver.dto.SingUpRequestDTO;
import com.alex.futurity.authorizationserver.exception.ErrorMessage;
import com.alex.futurity.authorizationserver.service.EmailSenderService;
import com.alex.futurity.authorizationserver.utils.HttpHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.mail.MailSendException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ErrorsHandlingTest extends Configurator {

    @MockBean
    private HttpHelper helper;

    @MockBean
    private EmailSenderService emailService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testIllegalStateException() {
        String message = "Something went wrong";
        LoginRequestDTO dto = new LoginRequestDTO("alex@jpeg.com", "alexRoot");
        when(helper.doPost(anyString(), any(), any())).thenThrow(new IllegalStateException(message));

        ResponseEntity<ErrorMessage> response = restTemplate.postForEntity(url + "/login/", dto, ErrorMessage.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo(new ErrorMessage(message));
    }

    @Test
    void testMethodArgumentNotValidException() {
        LoginRequestDTO dto = new LoginRequestDTO("alex@jpeg.com", "al");
        ResponseEntity<ErrorMessage> response = restTemplate.postForEntity(url + "/login/", dto, ErrorMessage.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody())
                .isEqualTo(new ErrorMessage("Wrong password. Password must be more than 6 and less 64 characters"));
    }

    @Test
    void testFileSizeLimitExceededException() {
        SingUpRequestDTO dto = new SingUpRequestDTO("alex@jpeg.com", "alex", "alexRoot");
        MockMultipartFile file = new MockMultipartFile("avatar", "avatar.jpeg",
                MediaType.IMAGE_JPEG_VALUE, new byte[1024 * 1024 * 20]);
        Map<String, List<Object>> fields = Map.of(
                "avatar", List.of(file.getResource()), "user", List.of(dto)
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>(fields);

        ResponseEntity<ErrorMessage> response = restTemplate.postForEntity(url + "/singup/", new HttpEntity<>(body, headers), ErrorMessage.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.PAYLOAD_TOO_LARGE);
        assertThat(response.getBody())
                .isEqualTo(new ErrorMessage("Avatar is too large. Max size 5MB"));
    }

    @Test
    void testMailSendException() {
        when(helper.doPost(anyString(), any(), any())).thenReturn(Boolean.FALSE);
        doThrow(new MailSendException("")).when(emailService).sendConfirmationMessage(anyString(), anyString());
        ConfirmEmailRequestDTO dto = new ConfirmEmailRequestDTO("alex@jpeg.com");

        ResponseEntity<ErrorMessage> response = restTemplate.postForEntity(url + "/confirm-email/", dto, ErrorMessage.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo(new ErrorMessage("Failed to send email. Try again after a while"));
    }
}

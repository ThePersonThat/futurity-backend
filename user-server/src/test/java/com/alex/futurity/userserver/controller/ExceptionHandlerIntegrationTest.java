package com.alex.futurity.userserver.controller;

import com.alex.futurity.userserver.dto.SingUpRequestDTO;
import com.alex.futurity.userserver.exception.CannotUploadFileException;
import com.alex.futurity.userserver.exception.ErrorMessage;
import com.alex.futurity.userserver.service.AuthService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExceptionHandlerIntegrationTest extends AuthConfigurator {
    @MockBean
    private AuthService authService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("SingUp: Should return 500 INTERNAL SERVER ERROR if the avatar cannot be read")
    @SneakyThrows
    void testSingUpIfAvatarCannotBeRead() {
        String message = "The avatar cannot be read";
        SingUpRequestDTO dto = new SingUpRequestDTO("alex@jpeg.com", "alex", "alexRoot", null);
        HttpEntity<MultiValueMap<String, Object>> body = buildMultiPartHttpEntity(dto, validAvatar);

        when(authService.singUp(any())).thenThrow(new CannotUploadFileException(message));

        ResponseEntity<ErrorMessage> response = restTemplate.postForEntity(url + "/singup/", body, ErrorMessage.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo(new ErrorMessage(message));
    }
}

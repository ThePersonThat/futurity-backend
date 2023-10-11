package com.alex.futurity.userserver.controller;

import com.alex.futurity.userserver.dto.SingUpRequestDto;
import com.alex.futurity.userserver.exception.CannotUploadFileException;
import com.alex.futurity.userserver.exception.ErrorMessage;
import com.alex.futurity.userserver.service.AuthService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockPart;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ExceptionHandlerIntegrationTest extends AuthConfigurator {
    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("SingUp: Should return 500 INTERNAL SERVER ERROR if the avatar cannot be read")
    @SneakyThrows
    void testSingUpIfAvatarCannotBeRead() {
        String message = "The avatar cannot be read";
        SingUpRequestDto dto = new SingUpRequestDto("alex@jpeg.com", "alex", "alexRoot", null);

        when(authService.singUp(any())).thenThrow(new CannotUploadFileException(message));

        mockMvc.perform(multipart("/singup")
                        .part(new MockPart("avatar", VALID_AVATAR.getBytes()), new MockPart("user", objectMapper.writeValueAsBytes(dto))))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json(objectMapper.writeValueAsString(new ErrorMessage(message))));
    }
}

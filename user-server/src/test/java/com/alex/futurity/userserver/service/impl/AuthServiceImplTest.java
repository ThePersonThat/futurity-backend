package com.alex.futurity.userserver.service.impl;

import com.alex.futurity.userserver.dto.LoginRequestDTO;
import com.alex.futurity.userserver.dto.LoginResponseDTO;
import com.alex.futurity.userserver.dto.SingUpRequestDTO;
import com.alex.futurity.userserver.entity.User;
import com.alex.futurity.userserver.exception.CannotUploadFileException;
import com.alex.futurity.userserver.exception.UserAlreadyExistException;
import com.alex.futurity.userserver.exception.UserNotFoundException;
import com.alex.futurity.userserver.service.UserService;
import lombok.SneakyThrows;
import nl.altindag.log.LogCaptor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private final String email = "alex@jpeg.com";
    private final String password = "alexroot";
    private final String nickname = "alex";
    private final User mockUser = new User(1L, email, nickname, password, new byte[100]);

    private final SingUpRequestDTO singUpMockDTO =
            new SingUpRequestDTO(email, nickname, password,
                    new MockMultipartFile("avatar", "user.jpeg",
                            MediaType.IMAGE_JPEG_VALUE, "mock".getBytes(StandardCharsets.UTF_8)));

    private final LoginRequestDTO loginMockDto = new LoginRequestDTO(email, password);

    @Test
    @SneakyThrows
    @DisplayName("Should sing up user")
    void testSingUp() {
        LogCaptor log = LogCaptor.forClass(AuthServiceImpl.class);
        when(encoder.encode(anyString())).thenReturn("Encoded Password");

        User user = authService.singUp(singUpMockDTO);

        verify(userService).saveUser(eq(user));
        verify(encoder).encode(eq(singUpMockDTO.getPassword()));

        assertThat(log.getLogs()).hasSize(1)
                .contains(String.format("User %s have been registered", user));

        assertThat(user.getEmail()).isEqualTo(singUpMockDTO.getEmail());
        assertThat(user.getNickname()).isEqualTo(singUpMockDTO.getNickname());
        assertThat(user.getAvatar()).isEqualTo(singUpMockDTO.getAvatar().getBytes());
        assertThat(user.getPassword()).isNotBlank();
    }

    @Test
    @DisplayName("Should throw the UserAlreadyException if the user already exists")
    void testSingUpIfUserExists() {
        when(userService.isUserExist(anyString())).thenReturn(true);

        assertThatThrownBy(() -> authService.singUp(singUpMockDTO))
                .isInstanceOf(UserAlreadyExistException.class)
                .hasMessage("Error. A user with the same email address already exists");
    }

    @Test
    @DisplayName("Should login user")
    void testLogin() {
        Optional<User> optionalUser = Optional.of(mockUser);
        when(userService.findUserByEmail(anyString())).thenReturn(optionalUser);
        when(encoder.matches(anyString(), anyString())).thenReturn(true);

        LoginResponseDTO loginResponse = authService.login(loginMockDto);

        assertThat(loginResponse.getNickname()).isEqualTo(mockUser.getNickname());
        assertThat(loginResponse.getId()).isEqualTo(mockUser.getId());
    }

    @Test
    @DisplayName("Should throw the UserNotFoundException if an email not found")
    void testLoginEmailNotFound() {
        when(userService.findUserByEmail(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(loginMockDto))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("Should throw the UserNotFoundException if a password does not match")
    void testLoginIfPasswordDoesNotMatch() {
        when(userService.findUserByEmail(anyString())).thenReturn(Optional.of(mockUser));
        when(encoder.matches(anyString(), anyString())).thenReturn(false);

        assertThatThrownBy(() -> authService.login(loginMockDto))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("Should trow the IOException if an avatar cannot be read")
    @SneakyThrows
    void testSingUpWithIOException() {
        String exceptionMessage = "Something went wrong";
        LogCaptor log = LogCaptor.forClass(AuthServiceImpl.class);
        SingUpRequestDTO dto = mock(SingUpRequestDTO.class);
        when(dto.toUser()).thenThrow(new IOException(exceptionMessage));

        assertThatThrownBy(() -> authService.singUp(dto))
                .isInstanceOf(CannotUploadFileException.class)
                .hasMessage("The avatar cannot be read");

        assertThat(log.getLogs()).hasSize(1)
                .contains(String.format("The avatar %s cannot be read: %s", dto.getAvatar(), exceptionMessage));
    }
}
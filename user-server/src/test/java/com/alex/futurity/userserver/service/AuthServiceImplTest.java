package com.alex.futurity.userserver.service;

import com.alex.futurity.userserver.dto.LoginRequestDto;
import com.alex.futurity.userserver.dto.LoginResponseDto;
import com.alex.futurity.userserver.dto.SingUpRequestDto;
import com.alex.futurity.userserver.entity.User;
import com.alex.futurity.userserver.exception.CannotUploadFileException;
import com.alex.futurity.userserver.exception.ClientSideException;
import lombok.SneakyThrows;

import org.jetbrains.annotations.NotNull;
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
    private AuthService authService;

    private static final String EMAIL = "alex@jpeg.com";
    private static final String PASSWORD = "alexroot";
    private static final String NICKNAME = "alex";

    @Test
    @SneakyThrows
    @DisplayName("Should sing up user")
    void testSingUp() {
        SingUpRequestDto singUpDto = getSingUpDto();
        when(encoder.encode(anyString())).thenReturn("Encoded Password");

        User user = authService.singUp(singUpDto);

        verify(userService).saveUser(eq(user));
        verify(encoder).encode(eq(singUpDto.getPassword()));

        assertThat(user.getEmail()).isEqualTo(singUpDto.getEmail());
        assertThat(user.getNickname()).isEqualTo(singUpDto.getNickname());
        assertThat(user.getAvatar()).isEqualTo(singUpDto.getAvatar().getBytes());
        assertThat(user.getPassword()).isNotBlank();
    }

    @Test
    @DisplayName("Should throw the UserAlreadyException if the user already exists")
    void testSingUpIfUserExists() {
        when(userService.isUserExist(anyString())).thenReturn(true);

        assertThatThrownBy(() -> authService.singUp(getSingUpDto()))
                .isInstanceOf(ClientSideException.class)
                .hasMessage("Error. A user with the same email address already exists");
    }

    @Test
    @DisplayName("Should login user")
    void testLogin() {
        User user = getUser();
        when(userService.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(encoder.matches(anyString(), anyString())).thenReturn(true);

        LoginResponseDto loginResponse = authService.login(getLoginRequestDto());

        assertThat(loginResponse.getNickname()).isEqualTo(user.getNickname());
        assertThat(loginResponse.getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("Should throw the UserNotFoundException if an email not found")
    void testLoginEmailNotFound() {
        when(userService.findUserByEmail(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(getLoginRequestDto()))
                .isInstanceOf(ClientSideException.class);
    }

    @Test
    @DisplayName("Should throw the UserNotFoundException if a password does not match")
    void testLoginIfPasswordDoesNotMatch() {
        when(userService.findUserByEmail(anyString())).thenReturn(Optional.of(getUser()));
        when(encoder.matches(anyString(), anyString())).thenReturn(false);

        assertThatThrownBy(() -> authService.login(getLoginRequestDto()))
                .isInstanceOf(ClientSideException.class);
    }

    @Test
    @DisplayName("Should trow the IOException if an avatar cannot be read")
    @SneakyThrows
    void testSingUpWithIOException() {
        String exceptionMessage = "Something went wrong";
        SingUpRequestDto dto = mock(SingUpRequestDto.class);
        when(dto.toUser()).thenThrow(new IOException(exceptionMessage));

        assertThatThrownBy(() -> authService.singUp(dto))
                .isInstanceOf(CannotUploadFileException.class)
                .hasMessage("The avatar cannot be read");
    }

    private static SingUpRequestDto getSingUpDto() {
        return new SingUpRequestDto(EMAIL, NICKNAME, PASSWORD,
                new MockMultipartFile("avatar", "user.jpeg",
                        MediaType.IMAGE_JPEG_VALUE, "mock".getBytes(StandardCharsets.UTF_8)));
    }

    private static User getUser() {
        return new User(1L, EMAIL, NICKNAME, PASSWORD, new byte[100]);
    }

    private static LoginRequestDto getLoginRequestDto() {
        return new LoginRequestDto(EMAIL, PASSWORD);
    }
}
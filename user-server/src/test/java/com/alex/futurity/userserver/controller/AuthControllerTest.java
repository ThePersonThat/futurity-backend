package com.alex.futurity.userserver.controller;

import com.alex.futurity.userserver.dto.LoginRequestDTO;
import com.alex.futurity.userserver.dto.LoginResponseDTO;
import com.alex.futurity.userserver.dto.SingUpRequestDTO;
import com.alex.futurity.userserver.entity.User;
import com.alex.futurity.userserver.exception.CannotUploadFileException;
import com.alex.futurity.userserver.exception.UserAlreadyExistException;
import com.alex.futurity.userserver.exception.UserNotFoundException;
import com.alex.futurity.userserver.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;

import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    private final String validEmail = "alex@gmail.com";
    private final String validNickname = "alex";
    private final String validPassword = "alexRoot";
    private final MockMultipartFile validAvatar = new MockMultipartFile("avatar", "user.jpeg",
            MediaType.IMAGE_JPEG_VALUE, new byte[1]);

    @Test
    @DisplayName("SingUp: Should return 201 CREATED if input is valid")
    @SneakyThrows
    void testValidSingUp() {
        SingUpRequestDTO dto = new SingUpRequestDTO(validEmail, validNickname, validPassword, null);
        MockMultipartFile dtoMockFile = new MockMultipartFile("user", "",
                MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(dto));

        mockMvc.perform(
                MockMvcRequestBuilders.multipart("/singup")
                        .file(validAvatar)
                        .file(dtoMockFile)
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    private static Stream<Arguments> invalidAvatars() {
        MockMultipartFile fileEmpty = new MockMultipartFile("avatar", "avatar.jpeg", MediaType.IMAGE_JPEG_VALUE,
                new byte[0]);
        MockMultipartFile largeFile = new MockMultipartFile("avatar", "avatar.jpeg", MediaType.IMAGE_JPEG_VALUE,
                new byte[6 * (1024 * 1024)]);
        System.out.println(largeFile.getSize());
        MockMultipartFile wrongType = new MockMultipartFile("avatar", "avatar.docx", MediaType.IMAGE_JPEG_VALUE,
                new byte[10]);
        MockMultipartFile withoutType = new MockMultipartFile("avatar", "avatar", MediaType.IMAGE_JPEG_VALUE,
                new byte[10]);

        return Stream.of(
                Arguments.of(fileEmpty, "Avatar must not be empty"),
                Arguments.of(largeFile, "Avatar is too large. Max size 5MB"),
                Arguments.of(wrongType, "Wrong image type. Must be one of the following: .jpeg, .png, .gif"),
                Arguments.of(withoutType, "Wrong image type. Must be one of the following: .jpeg, .png, .gif")
        );
    }

    @ParameterizedTest
    @SneakyThrows
    @MethodSource("invalidAvatars")
    @DisplayName("SingUp: Should return 400 BAD REQUEST with correct message if the avatar file is invalid")
    void testSingUpWithInvalidFile(MockMultipartFile avatar, String errorMessage) {
        SingUpRequestDTO dto = new SingUpRequestDTO(validEmail, validNickname, validPassword, null);
        MockMultipartFile dtoMockFile = new MockMultipartFile("user", "",
                MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(dto));

        mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/singup")
                                .file(avatar)
                                .file(dtoMockFile)
                ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{message: \"" + errorMessage + "\"}"));
    }

    private static Stream<Arguments> invalidEmails() {
        return Stream.of(
                Arguments.of(null, "Wrong email. Email must not be empty"),
                Arguments.of("", "Wrong email. Email must not be empty"),
                Arguments.of("alex.com", "Wrong email. Correct pattern: emailName@email.com")
        );
    }

    private static Stream<Arguments> invalidNicknames() {
        return Stream.of(
                Arguments.of(null, "Wrong nickname. Nickname must not be empty"),
                Arguments.of("      ", "Wrong nickname. Nickname must not be empty"),
                Arguments.of("al", "Wrong nickname. Nickname must be more than 4 and less 64 characters"),
                Arguments.of(RandomString.make(65), "Wrong nickname. Nickname must be more than 4 and less 64 characters")
        );
    }

    private static Stream<Arguments> invalidPasswords() {
        return Stream.of(
                Arguments.of(null, "Wrong password. Password must not be empty"),
                Arguments.of("      ", "Wrong password. Password must not be empty"),
                Arguments.of("alex", "Wrong password. Password must be more than 6 and less 64 characters"),
                Arguments.of(RandomString.make(65), "Wrong password. Password must be more than 6 and less 64 characters")
        );
    }

    @ParameterizedTest
    @SneakyThrows
    @MethodSource("invalidEmails")
    @DisplayName("SingUp: Should return 400 with correct message if the email is invalid")
    void testSingUpWithInvalidEmail(String invalidEmail, String errorMessage) {
        SingUpRequestDTO dto = new SingUpRequestDTO(invalidEmail, validNickname, validPassword, null);
        MockMultipartFile dtoMockFile = new MockMultipartFile("user", "",
                MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(dto));

        mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/singup")
                                .file(validAvatar)
                                .file(dtoMockFile)
                ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{message: \"" + errorMessage + "\"}"));
    }

    @ParameterizedTest
    @SneakyThrows
    @MethodSource("invalidNicknames")
    @DisplayName("SingUp: Should return 400 with correct message if the nickname is invalid")
    void testSingUpWithInvalidNickname(String invalidNickname, String errorMessage) {
        SingUpRequestDTO dto = new SingUpRequestDTO(validEmail, invalidNickname, validPassword, null);
        MockMultipartFile dtoMockFile = new MockMultipartFile("user", "",
                MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(dto));

        mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/singup")
                                .file(validAvatar)
                                .file(dtoMockFile)
                ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{message: \"" + errorMessage + "\"}"));
    }

    @ParameterizedTest
    @SneakyThrows
    @MethodSource("invalidPasswords")
    @DisplayName("SingUp: Should return 400 with correct message if the password is invalid")
    void testSingUpWithInvalidPassword(String invalidPassword, String errorMessage) {
        SingUpRequestDTO dto = new SingUpRequestDTO(validEmail, validNickname, invalidPassword, null);
        MockMultipartFile dtoMockFile = new MockMultipartFile("user", "",
                MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(dto));

        mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/singup")
                                .file(validAvatar)
                                .file(dtoMockFile)
                ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{message: \"" + errorMessage + "\"}"));
    }

    @Test
    @DisplayName("SingUp: Should call all business logic inside")
    @SneakyThrows
    void testSingUpLogic() {
        SingUpRequestDTO dto = new SingUpRequestDTO(validEmail, validNickname, validPassword, null);
        MockMultipartFile dtoMockFile = new MockMultipartFile("user", "",
                MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(dto));

        mockMvc.perform(
                MockMvcRequestBuilders.multipart("/singup")
                        .file(validAvatar)
                        .file(dtoMockFile)
        );

        ArgumentCaptor<SingUpRequestDTO> captor = ArgumentCaptor.forClass(SingUpRequestDTO.class);
        verify(authService).singUp(captor.capture());
        Assertions.assertThat(captor.getValue().getAvatar()).isEqualTo(validAvatar);
    }

    @Test
    @DisplayName("SingUp: Should return 500 INTERNAL SERVER ERROR if the avatar cannot be read")
    @SneakyThrows
    void testSingUpIfAvatarCannotBeRead() {
        SingUpRequestDTO dto = new SingUpRequestDTO(validEmail, validNickname, validPassword, null);
        MockMultipartFile dtoMockFile = new MockMultipartFile("user", "",
                MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(dto));
        String message = "The avatar cannot be read";

        when(authService.singUp(any(SingUpRequestDTO.class)))
                .thenThrow(new CannotUploadFileException(message));

        mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/singup")
                                .file(validAvatar)
                                .file(dtoMockFile)
                ).andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().json("{message: \"" + message + "\"}"));
    }

    @Test
    @DisplayName("SingUp: Should return 409 CONFLICT if the user already exists")
    @SneakyThrows
    void testSingUpIfUserAlreadyExists() {
        SingUpRequestDTO dto = new SingUpRequestDTO(validEmail, validNickname, validPassword, null);
        MockMultipartFile dtoMockFile = new MockMultipartFile("user", "",
                MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(dto));
        String message = "Error. A user with the same email address already exists";

        when(authService.singUp(any(SingUpRequestDTO.class)))
                .thenThrow(new UserAlreadyExistException());

        mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/singup")
                                .file(validAvatar)
                                .file(dtoMockFile)
                ).andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.content().json("{message: \"" + message + "\"}"));
    }

    @Test
    @DisplayName("Login: Should return 200 OK with body if input is valid")
    @SneakyThrows
    void testLoginWithValidInput() {
        LoginRequestDTO dto = new LoginRequestDTO(validEmail, validPassword);
        User user = new User();
        user.setId(1L);
        user.setNickname("alex");
        LoginResponseDTO expectedDto = new LoginResponseDTO(user);

        when(authService.login(any(LoginRequestDTO.class))).thenReturn(expectedDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/login")
                                .content(objectMapper.writeValueAsString(dto))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedDto)));

        verify(authService).login(eq(dto));
    }

    @ParameterizedTest
    @DisplayName("Login: Should return 400 with correct message if the email is invalid")
    @MethodSource("invalidEmails")
    @SneakyThrows
    void testLoginWithInvalidEmail(String invalidEmail, String errorMessage) {
        LoginRequestDTO dto = new LoginRequestDTO(invalidEmail, validPassword);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/login")
                                .content(objectMapper.writeValueAsString(dto))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{message: \"" + errorMessage + "\"}"));
    }

    @ParameterizedTest
    @DisplayName("Login: Should return 400 with correct message if the password is invalid")
    @MethodSource("invalidPasswords")
    @SneakyThrows
    void testLoginWithInvalidPassword(String invalidPassword, String errorMessage) {
        LoginRequestDTO dto = new LoginRequestDTO(validEmail, invalidPassword);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/login")
                                .content(objectMapper.writeValueAsString(dto))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{message: \"" + errorMessage + "\"}"));
    }

    @Test
    @DisplayName("Login: Should return 404 NOT FOUND if the user does not exist")
    @SneakyThrows
    void testLoginIfUserDoesNotExist() {
        LoginRequestDTO dto = new LoginRequestDTO(validEmail, validPassword);
        String message = "Email or password is incorrect. Check the entered data";

        when(authService.login(any(LoginRequestDTO.class)))
                .thenThrow(new UserNotFoundException());

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/login")
                                .content(objectMapper.writeValueAsString(dto))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                ).andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("{message: \"" + message + "\"}"));
    }

}
package com.alex.futurity.userserver.controller;

import com.alex.futurity.userserver.dto.LoginRequestDto;
import com.alex.futurity.userserver.dto.LoginResponseDto;
import com.alex.futurity.userserver.dto.SingUpRequestDto;
import com.alex.futurity.userserver.dto.UserExistRequestDto;
import com.alex.futurity.userserver.entity.User;
import com.alex.futurity.userserver.exception.ErrorMessage;
import com.alex.futurity.userserver.repo.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerIntegrationTest extends AuthConfigurator {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("SingUp: Should return 201 CREATED if input is valid and create the user in database")
    @SneakyThrows
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testValidSingUp() {
        SingUpRequestDto dto = new SingUpRequestDto(VALID_EMAIL, VALID_NICKNAME, VALID_PASSWORD, null);

        mockMvc.perform(multipart("/singup")
                        .file(VALID_AVATAR)
                        .part(buildUserPart(dto)))
                .andExpect(status().isCreated());

        List<User> users = userRepository.findAll();
        assertThat(users)
                .singleElement()
                .returns(dto.getEmail(), User::getEmail)
                .returns(dto.getNickname(), User::getNickname)
                .returns(VALID_AVATAR.getBytes(), User::getAvatar)
                .satisfies(user ->
                        assertThat(user.getPassword()).isNotBlank()
                                .isNotEmpty()
                                .isNotEqualTo(dto.getPassword()));
    }

    @ParameterizedTest
    @SneakyThrows
    @MethodSource("invalidRegistrationData")
    @DisplayName("SingUp: Should return 400 BAD REQUEST with correct message if the entered data is invalid")
    void testSingUpWithInvalidFile(SingUpRequestDto dto, MockMultipartFile avatar, ErrorMessage errorMessage) {
        mockMvc.perform(multipart("/singup")
                        .file(avatar)
                        .part(buildUserPart(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(objectMapper.writeValueAsString(errorMessage)));
    }

    @Test
    @DisplayName("SingUp: Should return 409 CONFLICT if the user already exists")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @SneakyThrows
    void testSingUpIfUserAlreadyExists() {
        SingUpRequestDto dto = new SingUpRequestDto(VALID_EMAIL, VALID_NICKNAME, VALID_PASSWORD, null);
        registerUser(new SingUpRequestDto(VALID_EMAIL, VALID_NICKNAME, VALID_PASSWORD, VALID_AVATAR));

        mockMvc.perform(multipart("/singup")
                        .file(VALID_AVATAR)
                        .part(buildUserPart(dto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
                )
                .andExpect(status().isConflict())
                .andExpect(content().json(objectMapper.writeValueAsString(new ErrorMessage("Error. A user with the same email address already exists"))));
    }

    @Test
    @DisplayName("Login: Should return 200 OK with body if input is valid")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @SneakyThrows
    void testLoginWithValidInput() {
        LoginRequestDto loginDto = new LoginRequestDto(VALID_EMAIL, VALID_PASSWORD);
        SingUpRequestDto singupDto = new SingUpRequestDto(VALID_EMAIL, VALID_NICKNAME, VALID_PASSWORD, VALID_AVATAR);
        registerUser(singupDto);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new LoginResponseDto(1L, singupDto.getNickname()))));
    }

    @Test
    @DisplayName("Login: Should return 404 NOT FOUND if the user does not exist")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @SneakyThrows
    void testLoginIfUserDoesNotExist() {
        LoginRequestDto dto = new LoginRequestDto(VALID_EMAIL, VALID_PASSWORD);
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(new ErrorMessage("Email or password is incorrect. Check the entered data"))));
    }

    @ParameterizedTest
    @DisplayName("Login: Should return 400 with correct message if the email is invalid")
    @MethodSource("invalidLoginData")
    @SneakyThrows
    void testLoginWithInvalidEmail(LoginRequestDto dto, ErrorMessage errorMessage) {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(errorMessage)));
    }

    @Test
    @DisplayName("Should return true if user exists")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @SneakyThrows
    void testExistIfUserExists() {
        UserExistRequestDto dto = new UserExistRequestDto(VALID_EMAIL);
        SingUpRequestDto singupDto = new SingUpRequestDto(VALID_EMAIL, VALID_NICKNAME, VALID_PASSWORD, VALID_AVATAR);
        registerUser(singupDto);

        mockMvc.perform(post("/exist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string(Boolean.TRUE.toString()));
    }

    @Test
    @DisplayName("Should return false if user does not exist")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @SneakyThrows
    void testExistIfUserDoesNotExist() {
        UserExistRequestDto dto = new UserExistRequestDto(VALID_EMAIL);

        mockMvc.perform(post("/exist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string(Boolean.FALSE.toString()));
    }


    @SneakyThrows
    private void registerUser(SingUpRequestDto dto) {
        User user = dto.toUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
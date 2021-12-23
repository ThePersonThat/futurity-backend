package com.alex.futurity.userserver.controller;

import com.alex.futurity.userserver.dto.LoginRequestDTO;
import com.alex.futurity.userserver.dto.LoginResponseDTO;
import com.alex.futurity.userserver.dto.SingUpRequestDTO;
import com.alex.futurity.userserver.entity.User;
import com.alex.futurity.userserver.exception.ErrorMessage;
import com.alex.futurity.userserver.repo.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class AuthControllerIntegrationTest extends AuthConfigurator {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("SingUp: Should return 201 CREATED if input is valid and create the user in database")
    @SneakyThrows
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testValidSingUp() {
        SingUpRequestDTO dto = new SingUpRequestDTO(validEmail, validNickname, validPassword, null);
        HttpEntity<MultiValueMap<String, Object>> body = buildMultiPartHttpEntity(dto, validAvatar);

        ResponseEntity<String> response = restTemplate.postForEntity(url + "/singup/", body, String.class);

        List<User> users = userRepository.findAll();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNullOrEmpty();
        assertThat(users)
                .hasSize(1);

        User user = users.get(0);
        assertThat(user.getEmail()).isEqualTo(dto.getEmail());
        assertThat(user.getNickname()).isEqualTo(dto.getNickname());
        assertThat(validAvatar.getBytes()).isEqualTo(user.getAvatar());
        assertThat(user.getPassword()).isNotBlank()
                .isNotEmpty()
                .isNotEqualTo(dto.getPassword());
    }

    @ParameterizedTest
    @SneakyThrows
    @MethodSource("invalidRegistrationData")
    @DisplayName("SingUp: Should return 400 BAD REQUEST with correct message if the entered data is invalid")
    void testSingUpWithInvalidFile(SingUpRequestDTO dto, MockMultipartFile avatar, ErrorMessage errorMessage) {
        HttpEntity<MultiValueMap<String, Object>> body = buildMultiPartHttpEntity(dto, avatar);
        ResponseEntity<ErrorMessage> response = restTemplate.postForEntity(url + "/singup/", body, ErrorMessage.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo(errorMessage);
    }

    @Test
    @DisplayName("SingUp: Should return 409 CONFLICT if the user already exists")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @SneakyThrows
    void testSingUpIfUserAlreadyExists() {
        SingUpRequestDTO dto = new SingUpRequestDTO(validEmail, validNickname, validPassword, null);
        registerUser(new SingUpRequestDTO(validEmail, validNickname, validPassword, validAvatar));
        HttpEntity<MultiValueMap<String, Object>> body = buildMultiPartHttpEntity(dto, validAvatar);

        ResponseEntity<ErrorMessage> response = restTemplate.postForEntity(url + "/singup/", body, ErrorMessage.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody())
                .isEqualTo(new ErrorMessage("Error. A user with the same email address already exists"));
    }

    @Test
    @DisplayName("Login: Should return 200 OK with body if input is valid")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @SneakyThrows
    void testLoginWithValidInput() {
        LoginRequestDTO loginDto = new LoginRequestDTO(validEmail, validPassword);
        SingUpRequestDTO singupDto = new SingUpRequestDTO(validEmail, validNickname, validPassword, validAvatar);
        registerUser(singupDto);

        ResponseEntity<LoginResponseDTO> response = restTemplate.postForEntity(url + "/login/",
                loginDto, LoginResponseDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(new LoginResponseDTO(1L, singupDto.getNickname()));
    }

    @Test
    @DisplayName("Login: Should return 404 NOT FOUND if the user does not exist")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @SneakyThrows
    void testLoginIfUserDoesNotExist() {
        LoginRequestDTO dto = new LoginRequestDTO(validEmail, validPassword);
        ResponseEntity<ErrorMessage> response = restTemplate.postForEntity(url + "/login/", dto, ErrorMessage.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody())
                .isEqualTo(new ErrorMessage("Email or password is incorrect. Check the entered data"));
    }

    @ParameterizedTest
    @DisplayName("Login: Should return 400 with correct message if the email is invalid")
    @MethodSource("invalidLoginData")
    @SneakyThrows
    void testLoginWithInvalidEmail(LoginRequestDTO dto, ErrorMessage errorMessage) {
        ResponseEntity<ErrorMessage> response = restTemplate.postForEntity(url + "/login/", dto, ErrorMessage.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo(errorMessage);
    }

    @SneakyThrows
    private void registerUser(SingUpRequestDTO dto) {
        User user = dto.toUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
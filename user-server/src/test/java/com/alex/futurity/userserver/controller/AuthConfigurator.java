package com.alex.futurity.userserver.controller;

import com.alex.futurity.userserver.dto.LoginRequestDto;
import com.alex.futurity.userserver.dto.SingUpRequestDto;
import com.alex.futurity.userserver.exception.ErrorMessage;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.stream.Stream;

@SpringBootTest
@ContextConfiguration(initializers = AuthControllerIntegrationTest.Initializer.class)
@Testcontainers
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class AuthConfigurator {
    protected static final String VALID_EMAIL = "alex@gmail.com";
    protected static final String VALID_NICKNAME = "alex";
    protected static final String VALID_PASSWORD = "alexRoot";
    protected static final MockMultipartFile VALID_AVATAR =
            new MockMultipartFile("avatar", "user.jpeg", MediaType.IMAGE_JPEG_VALUE, new byte[1]);

    @Autowired
    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper = new ObjectMapper();

    @Container
    private final static PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:latest")
                    .withUsername("postgres")
                    .withPassword("root");

    protected static Stream<Arguments> invalidLoginData() {
        // invalid emails
        LoginRequestDto dtoWithNullEmail = new LoginRequestDto(null, VALID_PASSWORD);
        LoginRequestDto dtoWithEmptyEmail = new LoginRequestDto("", VALID_PASSWORD);
        LoginRequestDto dtoWithInvalidEmail = new LoginRequestDto("alex.com", VALID_PASSWORD);

        // invalid passwords
        LoginRequestDto dtoWithNullPassword = new LoginRequestDto(VALID_EMAIL, null);
        LoginRequestDto dtoWithBlankPassword = new LoginRequestDto(VALID_EMAIL, "        ");
        LoginRequestDto dtoWithSmallPassword = new LoginRequestDto(VALID_EMAIL, "alex");
        LoginRequestDto dtoWithBigPassword = new LoginRequestDto(VALID_EMAIL, RandomStringUtils.random(65));

        return Stream.of(
                // invalid emails
                Arguments.of(dtoWithNullEmail, new ErrorMessage("Wrong email. Email must not be empty")),
                Arguments.of(dtoWithEmptyEmail, new ErrorMessage("Wrong email. Email must not be empty")),
                Arguments.of(dtoWithInvalidEmail, new ErrorMessage("Wrong email. Correct pattern: emailName@email.com")),

                // invalid passwords
                Arguments.of(dtoWithNullPassword, new ErrorMessage("Wrong password. Password must not be empty")),
                Arguments.of(dtoWithBlankPassword, new ErrorMessage("Wrong password. Password must not be empty")),
                Arguments.of(dtoWithSmallPassword, new ErrorMessage("Wrong password. Password must be more than 6 and less 64 characters")),
                Arguments.of(dtoWithBigPassword, new ErrorMessage("Wrong password. Password must be more than 6 and less 64 characters"))
        );
    }

    protected static Stream<Arguments> invalidRegistrationData() {
        MockMultipartFile fileEmpty = new MockMultipartFile("avatar", "avatar.jpeg", MediaType.IMAGE_JPEG_VALUE,
                new byte[0]);
        MockMultipartFile largeFile = new MockMultipartFile("avatar", "avatar.jpeg", MediaType.IMAGE_JPEG_VALUE,
                new byte[6 * (1024 * 1024)]);
        MockMultipartFile wrongType = new MockMultipartFile("avatar", "avatar.docx", MediaType.IMAGE_JPEG_VALUE,
                new byte[10]);
        MockMultipartFile withoutType = new MockMultipartFile("avatar", "avatar", MediaType.IMAGE_JPEG_VALUE,
                new byte[10]);

        SingUpRequestDto validDto = new SingUpRequestDto(VALID_EMAIL, VALID_NICKNAME, VALID_PASSWORD, null);

        // invalid emails
        SingUpRequestDto dtoWithNullEmail = new SingUpRequestDto(null, VALID_NICKNAME, VALID_PASSWORD, null);
        SingUpRequestDto dtoWithEmptyEmail = new SingUpRequestDto("", VALID_NICKNAME, VALID_PASSWORD, null);
        SingUpRequestDto dtoWithInvalidEmail = new SingUpRequestDto("alex.com", VALID_NICKNAME, VALID_PASSWORD, null);

        // invalid nicknames
        SingUpRequestDto dtoWithNullNickname = new SingUpRequestDto(VALID_EMAIL, null, VALID_PASSWORD, null);
        SingUpRequestDto dtoWithBlankNickname = new SingUpRequestDto(VALID_EMAIL, "         ", VALID_PASSWORD, null);
        SingUpRequestDto dtoWithSmallNickname = new SingUpRequestDto(VALID_EMAIL, "al", VALID_PASSWORD, null);
        SingUpRequestDto dtoWithBigNickname = new SingUpRequestDto(VALID_EMAIL, RandomStringUtils.random(65), VALID_PASSWORD, null);

        // invalid passwords
        SingUpRequestDto dtoWithNullPassword = new SingUpRequestDto(VALID_EMAIL, VALID_NICKNAME, null, null);
        SingUpRequestDto dtoWithBlankPassword = new SingUpRequestDto(VALID_EMAIL, VALID_NICKNAME, "        ", null);
        SingUpRequestDto dtoWithSmallPassword = new SingUpRequestDto(VALID_EMAIL, VALID_NICKNAME, "alex", null);
        SingUpRequestDto dtoWithBigPassword = new SingUpRequestDto(VALID_EMAIL, VALID_NICKNAME, RandomStringUtils.random(65), null);

        return Stream.of(
                // invalid files
                Arguments.of(validDto, fileEmpty, new ErrorMessage("Avatar must not be empty")),
                Arguments.of(validDto, largeFile, new ErrorMessage("Avatar is too large. Max size 5MB")),
                Arguments.of(validDto, wrongType, new ErrorMessage("Wrong image type. Must be one of the following: .jpeg, .png, .gif")),
                Arguments.of(validDto, withoutType, new ErrorMessage("Wrong image type. Must be one of the following: .jpeg, .png, .gif")),

                // invalid emails
                Arguments.of(dtoWithNullEmail, VALID_AVATAR, new ErrorMessage("Wrong email. Email must not be empty")),
                Arguments.of(dtoWithEmptyEmail, VALID_AVATAR, new ErrorMessage("Wrong email. Email must not be empty")),
                Arguments.of(dtoWithInvalidEmail, VALID_AVATAR, new ErrorMessage("Wrong email. Correct pattern: emailName@email.com")),

                // invalid nicknames
                Arguments.of(dtoWithNullNickname, VALID_AVATAR, new ErrorMessage("Wrong nickname. Nickname must not be empty")),
                Arguments.of(dtoWithBlankNickname, VALID_AVATAR, new ErrorMessage("Wrong nickname. Nickname must not be empty")),
                Arguments.of(dtoWithSmallNickname, VALID_AVATAR, new ErrorMessage("Wrong nickname. Nickname must be more than 4 and less 64 characters")),
                Arguments.of(dtoWithBigNickname, VALID_AVATAR, new ErrorMessage("Wrong nickname. Nickname must be more than 4 and less 64 characters")),

                // invalid passwords
                Arguments.of(dtoWithNullPassword, VALID_AVATAR, new ErrorMessage("Wrong password. Password must not be empty")),
                Arguments.of(dtoWithBlankPassword, VALID_AVATAR, new ErrorMessage("Wrong password. Password must not be empty")),
                Arguments.of(dtoWithSmallPassword, VALID_AVATAR, new ErrorMessage("Wrong password. Password must be more than 6 and less 64 characters")),
                Arguments.of(dtoWithBigPassword, VALID_AVATAR, new ErrorMessage("Wrong password. Password must be more than 6 and less 64 characters"))
        );
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
                    "spring.datasource.url=" + postgresContainer.getJdbcUrl(),
                    "spring.datasource.password=" + postgresContainer.getPassword(),
                    "spring.datasource.username=" + postgresContainer.getUsername()
            );

            values.applyTo(applicationContext);
        }
    }

    @SneakyThrows
    protected <T> MockPart buildUserPart(T dto) {
        MockPart mockPart = new MockPart("user", objectMapper.writeValueAsBytes(dto));
        mockPart.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return mockPart;
    }
}

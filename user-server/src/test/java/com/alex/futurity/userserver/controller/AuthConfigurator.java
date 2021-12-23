package com.alex.futurity.userserver.controller;

import com.alex.futurity.userserver.UserServerApplication;
import com.alex.futurity.userserver.dto.LoginRequestDTO;
import com.alex.futurity.userserver.dto.SingUpRequestDTO;
import com.alex.futurity.userserver.exception.ErrorMessage;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = UserServerApplication.class)
@ContextConfiguration(initializers = AuthControllerIntegrationTest.Initializer.class)
@Testcontainers
@ExtendWith({SpringExtension.class})
public class AuthConfigurator {
    protected static final String validEmail = "alex@gmail.com";
    protected static final String validNickname = "alex";
    protected static final String validPassword = "alexRoot";
    protected static final MockMultipartFile validAvatar = new MockMultipartFile("avatar", "user.jpeg",
            MediaType.IMAGE_JPEG_VALUE, new byte[1]);
    protected String url;

    @LocalServerPort
    private int port;

    @Container
    private final static PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:latest")
                    .withUsername("postgres")
                    .withPassword("root");

    @PostConstruct
    private void initHost() {
        url = "http://localhost:" + port;
    }

    protected HttpEntity<MultiValueMap<String, Object>> buildMultiPartHttpEntity(Object dto, MockMultipartFile avatar) {
        Map<String, List<Object>> fields = Map.of(
                "avatar", List.of(avatar.getResource()), "user", List.of(dto)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>(fields);

        return new HttpEntity<>(body, headers);
    }

    protected static Stream<Arguments> invalidLoginData() {
        // invalid emails
        LoginRequestDTO dtoWithNullEmail = new LoginRequestDTO(null, validPassword);
        LoginRequestDTO dtoWithEmptyEmail = new LoginRequestDTO("", validPassword);
        LoginRequestDTO dtoWithInvalidEmail = new LoginRequestDTO("alex.com", validPassword);

        // invalid passwords
        LoginRequestDTO dtoWithNullPassword = new LoginRequestDTO(validEmail, null);
        LoginRequestDTO dtoWithBlankPassword = new LoginRequestDTO(validEmail, "        ");
        LoginRequestDTO dtoWithSmallPassword = new LoginRequestDTO(validEmail, "alex");
        LoginRequestDTO dtoWithBigPassword = new LoginRequestDTO(validEmail, RandomString.make(65));

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

        SingUpRequestDTO validDto = new SingUpRequestDTO(validEmail, validNickname, validPassword, null);

        // invalid emails
        SingUpRequestDTO dtoWithNullEmail = new SingUpRequestDTO(null, validNickname, validPassword, null);
        SingUpRequestDTO dtoWithEmptyEmail = new SingUpRequestDTO("", validNickname, validPassword, null);
        SingUpRequestDTO dtoWithInvalidEmail = new SingUpRequestDTO("alex.com", validNickname, validPassword, null);

        // invalid nicknames
        SingUpRequestDTO dtoWithNullNickname = new SingUpRequestDTO(validEmail, null, validPassword, null);
        SingUpRequestDTO dtoWithBlankNickname = new SingUpRequestDTO(validEmail, "         ", validPassword, null);
        SingUpRequestDTO dtoWithSmallNickname = new SingUpRequestDTO(validEmail, "al", validPassword, null);
        SingUpRequestDTO dtoWithBigNickname = new SingUpRequestDTO(validEmail, RandomString.make(65), validPassword, null);

        // invalid passwords
        SingUpRequestDTO dtoWithNullPassword = new SingUpRequestDTO(validEmail, validNickname, null, null);
        SingUpRequestDTO dtoWithBlankPassword = new SingUpRequestDTO(validEmail, validNickname, "        ", null);
        SingUpRequestDTO dtoWithSmallPassword = new SingUpRequestDTO(validEmail, validNickname, "alex", null);
        SingUpRequestDTO dtoWithBigPassword = new SingUpRequestDTO(validEmail, validNickname, RandomString.make(65), null);

        return Stream.of(
                // invalid files
                Arguments.of(validDto, fileEmpty, new ErrorMessage("Avatar must not be empty")),
                Arguments.of(validDto, largeFile, new ErrorMessage("Avatar is too large. Max size 5MB")),
                Arguments.of(validDto, wrongType, new ErrorMessage("Wrong image type. Must be one of the following: .jpeg, .png, .gif")),
                Arguments.of(validDto, withoutType, new ErrorMessage("Wrong image type. Must be one of the following: .jpeg, .png, .gif")),

                // invalid emails
                Arguments.of(dtoWithNullEmail, validAvatar, new ErrorMessage("Wrong email. Email must not be empty")),
                Arguments.of(dtoWithEmptyEmail, validAvatar, new ErrorMessage("Wrong email. Email must not be empty")),
                Arguments.of(dtoWithInvalidEmail, validAvatar, new ErrorMessage("Wrong email. Correct pattern: emailName@email.com")),

                // invalid nicknames
                Arguments.of(dtoWithNullNickname, validAvatar, new ErrorMessage("Wrong nickname. Nickname must not be empty")),
                Arguments.of(dtoWithBlankNickname, validAvatar, new ErrorMessage("Wrong nickname. Nickname must not be empty")),
                Arguments.of(dtoWithSmallNickname, validAvatar, new ErrorMessage("Wrong nickname. Nickname must be more than 4 and less 64 characters")),
                Arguments.of(dtoWithBigNickname, validAvatar, new ErrorMessage("Wrong nickname. Nickname must be more than 4 and less 64 characters")),

                // invalid passwords
                Arguments.of(dtoWithNullPassword, validAvatar, new ErrorMessage("Wrong password. Password must not be empty")),
                Arguments.of(dtoWithBlankPassword, validAvatar, new ErrorMessage("Wrong password. Password must not be empty")),
                Arguments.of(dtoWithSmallPassword, validAvatar, new ErrorMessage("Wrong password. Password must be more than 6 and less 64 characters")),
                Arguments.of(dtoWithBigPassword, validAvatar, new ErrorMessage("Wrong password. Password must be more than 6 and less 64 characters"))
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
}

package com.alex.futurity.authorizationserver.controller;

import com.alex.futurity.authorizationserver.domain.LoginDomain;
import com.alex.futurity.authorizationserver.dto.*;
import com.alex.futurity.authorizationserver.entity.ConfirmationToken;
import com.alex.futurity.authorizationserver.exception.ErrorMessage;
import com.alex.futurity.authorizationserver.repo.ConfirmationTokenRepository;
import com.alex.futurity.authorizationserver.utils.RsaReader;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuthControllerIntegrationTest extends Configurator {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmailManager manager;

    @Autowired
    private ConfirmationTokenRepository tokenRepo;

    @Autowired
    private RsaReader reader;

    @MockBean
    private RestTemplate mockRest;

    @BeforeEach
    private void clear() {
        manager.clearMessages();
    }

    @Test
    @DisplayName("Should send the message to the email and save token")
    @SneakyThrows
    void testConfirmEmail() {
        ConfirmEmailRequestDTO dto = new ConfirmEmailRequestDTO(manager.getEmail());
        when(mockRest.postForEntity(anyString(), any(), any())).thenReturn(ResponseEntity.ok(Boolean.FALSE));
        ResponseEntity<String> response = restTemplate.postForEntity(url + "/confirm-email/",
                dto, String.class);

        Message message = manager.checkCountMessagesAndGet(1, 0);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(message.getSubject()).isEqualTo("Confirm your email");
        assertThat(message.contains("Your registration code")).isTrue();
        String code = message.getTextFromMessage("\\d{6}");
        ConfirmationToken token = getToken();
        assertThat(token.getCode()).isEqualTo(code);
        assertThat(token.getEmail()).isEqualTo(manager.getEmail());
        assertThat(token.isConfirmed()).isFalse();
        assertThat(token.getConfirmedAt()).isNull();
    }

    @Test
    @DisplayName("Should return 409 Conflict if email exists")
    void testConfirmEmailIfEmailExist() {
        ConfirmEmailRequestDTO dto = new ConfirmEmailRequestDTO(manager.getEmail());
        ErrorMessage errorMessage = new ErrorMessage(String.format("%s already registered", dto.getEmail()));
        when(mockRest.postForEntity(anyString(), any(), any())).thenReturn(ResponseEntity.ok(Boolean.TRUE));
        ResponseEntity<ErrorMessage> response = restTemplate.postForEntity(url + "/confirm-email/",
                dto, ErrorMessage.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isEqualTo(errorMessage);
        assertThat(tokenRepo.findAll()).isEmpty();
    }

    @Test
    @DisplayName("Should confirm code")
    void testConfirmCode() {
        String code = "123456";
        ConfirmCodeRequestDTO dto = new ConfirmCodeRequestDTO(manager.getEmail(), code);
        createConfirmationToken(new ConfirmationToken(code, manager.getEmail(), LocalDateTime.now().plusHours(15)));
        ResponseEntity<String> response = restTemplate.postForEntity(url + "/confirm-code/", dto, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ConfirmationToken token = getToken();
        assertThat(token.isConfirmed()).isTrue();
        assertThat(token.getConfirmedAt()).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("getWrongConfirmationTokens")
    @DisplayName("Should return 404 Not found if code or email are wrong")
    void testConfirmCodeIfItIsWrong(ConfirmCodeRequestDTO dto) {
        createConfirmationToken(new ConfirmationToken("123456", manager.getEmail(), LocalDateTime.now().plusHours(15)));

        ResponseEntity<ErrorMessage> response = restTemplate.postForEntity(url + "/confirm-code/", dto, ErrorMessage.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(new ErrorMessage(String.format("Wrong code for %s. Check the code again", dto.getEmail())));
    }

    @Test
    @DisplayName("Should return jwt token if user exists")
    void testLogin() {
        LoginRequestDTO dto = new LoginRequestDTO(manager.getEmail(), manager.getPassword());
        LoginDomain loginDomain = new LoginDomain(1L, "alex");
        when(mockRest.postForEntity(anyString(), any(), any()))
                .thenReturn(ResponseEntity.ok(loginDomain));

        ResponseEntity<JwtTokenResponseDTO> response = restTemplate.postForEntity(url + "/login/", dto, JwtTokenResponseDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        String token = response.getBody().getToken();
        assertThat(token).isNotNull().isNotEmpty();
        Claims body = Jwts.parser().setSigningKey(reader.getPrivateKey())
                .parseClaimsJws(token).getBody();
        assertThat(body.getSubject()).isEqualTo(loginDomain.getId().toString());
    }

    @Test
    @DisplayName("Should return 403 Forbidden if email is not confirmed")
    void testSingUpIfEmailIsNotConfirmed() {
        SingUpRequestDTO dto = new SingUpRequestDTO(manager.getEmail(), "alex", manager.getPassword());
        createConfirmationToken(new ConfirmationToken("123456", manager.getEmail(), LocalDateTime.now().plusHours(15)));

        ResponseEntity<ErrorMessage> response = restTemplate.postForEntity(url + "/singup/", buildMultiPartHttpEntity(dto),
                ErrorMessage.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isEqualTo(new ErrorMessage(String.format("%s is not confirmed", dto.getEmail())));
    }

    @Test
    @DisplayName("Should register user")
    void testSingUp() {
        SingUpRequestDTO dto = new SingUpRequestDTO(manager.getEmail(), "alex", manager.getPassword());
        ConfirmationToken token = new ConfirmationToken("123456", manager.getEmail(), LocalDateTime.now().plusHours(15));
        token.setConfirmed(true);
        createConfirmationToken(token);
        when(mockRest.postForEntity(anyString(), any(), any()))
                .thenReturn(ResponseEntity.ok().build());

        ResponseEntity<String> response = restTemplate.postForEntity(url + "/singup/", buildMultiPartHttpEntity(dto), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    private static Stream<ConfirmCodeRequestDTO> getWrongConfirmationTokens() {
        return Stream.of(
                new ConfirmCodeRequestDTO("wrong@email.com", "123456"),
                new ConfirmCodeRequestDTO("alex@jpeg.com", "123457")
        );
    }

    private HttpEntity<MultiValueMap<String, Object>> buildMultiPartHttpEntity(SingUpRequestDTO dto) {
        MockMultipartFile file = new MockMultipartFile("avatar", "avatar.jpeg",
                MediaType.IMAGE_JPEG_VALUE, new byte[1]);
        Map<String, List<Object>> fields = Map.of(
                "avatar", List.of(file.getResource()), "user", List.of(dto)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>(fields);

        return new HttpEntity<>(body, headers);
    }

    private void createConfirmationToken(ConfirmationToken token) {
        tokenRepo.save(token);
    }

    private ConfirmationToken getToken() {
        List<ConfirmationToken> tokens = tokenRepo.findAll();
        assertThat(tokens).hasSize(1);
        return tokens.get(0);
    }
}
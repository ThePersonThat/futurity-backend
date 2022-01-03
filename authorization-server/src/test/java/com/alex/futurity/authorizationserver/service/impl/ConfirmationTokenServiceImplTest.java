package com.alex.futurity.authorizationserver.service.impl;

import com.alex.futurity.authorizationserver.dto.ConfirmCodeRequestDTO;
import com.alex.futurity.authorizationserver.entity.ConfirmationToken;
import com.alex.futurity.authorizationserver.exception.ClientSideException;
import com.alex.futurity.authorizationserver.repo.ConfirmationTokenRepository;
import com.alex.futurity.authorizationserver.service.ConfirmationTokenGenerator;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ConfirmationTokenServiceImplTest {
    @Mock
    private ConfirmationTokenRepository tokenRepository;
    @Mock
    private ConfirmationTokenGenerator tokenGenerator;
    @InjectMocks
    private ConfirmationTokenServiceImpl tokenService;

    private String email = "alex@jpeg.com";
    private String code = "123456";
    private ConfirmationToken token = new ConfirmationToken(code, email, LocalDateTime.now());

    @Test
    @DisplayName("Should generate a confirmation token")
    void testGenerateConfirmationToken() {
        when(tokenGenerator.generateToken(anyString())).thenReturn(token);

        String code = tokenService.generateConfirmationTokenForEmail(email);

        verify(tokenRepository).deleteByEmail(eq(email));
        verify(tokenGenerator).generateToken(eq(email));
        verify(tokenRepository).save(eq(token));
        assertThat(code).isEqualTo(this.code);
    }

    @Test
    @DisplayName("Should confirm token if a code exists")
    void testConfirmCode() {
        LogCaptor captor = LogCaptor.forClass(ConfirmationTokenServiceImpl.class);
        ConfirmCodeRequestDTO dto = new ConfirmCodeRequestDTO(email, code);
        Optional<ConfirmationToken> token = Optional.of(this.token);
        when(tokenRepository.findByEmailAndCodeAndConfirmedFalse(anyString(), anyString())).thenReturn(token);
        LocalDateTime now = LocalDateTime.now();

        tokenService.confirmToken(dto);

        assertThat(token.get().getConfirmedAt().truncatedTo(ChronoUnit.SECONDS))
                .isEqualTo(now.truncatedTo(ChronoUnit.SECONDS));
        assertThat(token.get().isConfirmed()).isTrue();
        assertThat(captor.getLogs())
                .hasSize(1)
                .contains(String.format("Code %s for %s have been confirmed", code, email));
    }

    @Test
    @DisplayName("Should throw a WrongTokenException if a code does not exist")
    public void testConfirmCodeIfItDoesNotExist() {
        ConfirmCodeRequestDTO dto = new ConfirmCodeRequestDTO(email, code);
        Optional<ConfirmationToken> token = Optional.empty();
        when(tokenRepository.findByEmailAndCodeAndConfirmedFalse(email, code)).thenReturn(token);

        assertThatThrownBy(() -> tokenService.confirmToken(dto))
                .isInstanceOf(ClientSideException.class)
                .hasMessage(String.format("Wrong code for %s. Check the code again", dto.getEmail()));
    }
}
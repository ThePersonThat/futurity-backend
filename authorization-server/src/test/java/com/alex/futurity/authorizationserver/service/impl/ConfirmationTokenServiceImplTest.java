package com.alex.futurity.authorizationserver.service.impl;

import com.alex.futurity.authorizationserver.entity.ConfirmationToken;
import com.alex.futurity.authorizationserver.exception.WrongTokenCodeException;
import com.alex.futurity.authorizationserver.repo.ConfirmationTokenRepository;
import com.alex.futurity.authorizationserver.service.ConfirmationTokenGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
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
        Optional<ConfirmationToken> token = Optional.of(this.token);
        when(tokenRepository.findByEmailAndCode(email, code)).thenReturn(token);

        tokenService.confirmToken(email, code);

        verify(tokenRepository).delete(token.get());
    }

    @Test
    @DisplayName("Should throw a WrongTokenException if a code does not exist")
    public void testConfirmCodeIfItDoesNotExist() {
        Optional<ConfirmationToken> token = Optional.empty();
        when(tokenRepository.findByEmailAndCode(email, code)).thenReturn(token);

        assertThatThrownBy(() -> tokenService.confirmToken(email, code))
                .isInstanceOf(WrongTokenCodeException.class)
                .hasMessage("Wrong code, check the code again");
    }
}
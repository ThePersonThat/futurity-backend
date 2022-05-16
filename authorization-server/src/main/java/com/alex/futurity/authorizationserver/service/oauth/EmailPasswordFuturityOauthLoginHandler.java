package com.alex.futurity.authorizationserver.service.oauth;

import com.alex.futurity.authorizationserver.dto.JwtRefreshResponseDTO;
import com.alex.futurity.authorizationserver.dto.LoginRequestDTO;
import com.alex.futurity.authorizationserver.exception.OauthLoginFailedException;
import com.alex.futurity.authorizationserver.service.LoginUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailPasswordFuturityOauthLoginHandler implements FuturityOauthLoginHandler {
    private final LoginUserService loginUserService;


    @Override
    public JwtRefreshResponseDTO loginUser(OauthLogin oauthLogin) throws OauthLoginFailedException {
        if (!(oauthLogin instanceof EmailPasswordOauthLogin))
            throw new OauthLoginFailedException("Wrong oauth login type! Try again later or contact us!");
        EmailPasswordOauthLogin emailPasswordOauthLogin = (EmailPasswordOauthLogin) oauthLogin;
        String email = emailPasswordOauthLogin.getEmail();
        String password = emailPasswordOauthLogin.getPassword();

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(email, password);
        return this.loginUserService.loginUser(loginRequestDTO);
    }
}

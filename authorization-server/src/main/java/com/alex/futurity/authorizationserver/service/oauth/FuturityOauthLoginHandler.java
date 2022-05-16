package com.alex.futurity.authorizationserver.service.oauth;

import com.alex.futurity.authorizationserver.dto.JwtRefreshResponseDTO;
import com.alex.futurity.authorizationserver.exception.OauthLoginFailedException;

public interface FuturityOauthLoginHandler {

    JwtRefreshResponseDTO loginUser(OauthLogin oauthLogin) throws OauthLoginFailedException;

}

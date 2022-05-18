package com.alex.futurity.authorizationserver.service.oauth;

import com.alex.futurity.authorizationserver.dto.OauthSuccessfulDTO;
import com.alex.futurity.authorizationserver.exception.OauthLoginFailedException;

public interface FuturityOauthLoginHandler {

    OauthSuccessfulDTO loginUser(OauthLogin oauthLogin) throws OauthLoginFailedException;

}

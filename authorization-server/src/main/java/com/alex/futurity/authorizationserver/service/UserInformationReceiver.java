package com.alex.futurity.authorizationserver.service;

import com.alex.futurity.authorizationserver.dto.OauthUserAuthenticationDTO;

public interface UserInformationReceiver {

    OauthUserAuthenticationDTO getUserInfo(String token);

}

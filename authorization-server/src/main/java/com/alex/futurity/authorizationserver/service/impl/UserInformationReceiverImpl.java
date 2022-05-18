package com.alex.futurity.authorizationserver.service.impl;

import com.alex.futurity.authorizationserver.dto.OauthUserAuthenticationDTO;
import com.alex.futurity.authorizationserver.service.UserInformationReceiver;
import com.alex.futurity.authorizationserver.utils.HttpHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserInformationReceiverImpl implements UserInformationReceiver {
    private final HttpHelper httpHelper;
    @Value("${oauth.user.ud.link}")
    private String userIdUrl;

    @Override
    public OauthUserAuthenticationDTO getUserInfo(String token) {
        return this.httpHelper.doGet(userIdUrl, OauthUserAuthenticationDTO.class, token);
    }
}

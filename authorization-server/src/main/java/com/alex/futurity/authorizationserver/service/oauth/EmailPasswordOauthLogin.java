package com.alex.futurity.authorizationserver.service.oauth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailPasswordOauthLogin extends OauthLogin {
    private String email;
    private String password;
}

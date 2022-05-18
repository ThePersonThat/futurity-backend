package com.alex.futurity.authorizationserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OauthSuccessfulDTO {
    private String userId;
    private String username;
    private JwtRefreshResponseDTO tokens;
}

package com.alex.futurity.authorizationserver.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt.properties")
public class JwtProperties {
    private String privateKeyPath;
    private int accessExpiredTime;
    private int refreshExpiredTime;
}

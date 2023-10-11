package com.alex.futurity.authorizationserver.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;

@Data
@Component
@ConfigurationProperties(prefix = "spring.mail")
public class EmailProperties {
    private Map<String, String> properties;
    private boolean sendingEnabled;
    private String clientSecret;
    private String username;

    public Properties getEmailProperties() {
        Properties props = new Properties();
        props.putAll(properties);

        return props;
    }
}

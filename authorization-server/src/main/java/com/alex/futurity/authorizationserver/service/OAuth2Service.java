package com.alex.futurity.authorizationserver.service;

import com.alex.futurity.authorizationserver.properties.EmailProperties;
import com.alex.futurity.authorizationserver.utils.FileReader;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
public class OAuth2Service {
    private final EmailProperties emailProperties;
    private InputStream inputStream;

    public OAuth2Service(EmailProperties emailProperties) {
        this.emailProperties = emailProperties;
    }

    public String getAccessToken() {
        try {
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(inputStream);
            googleCredentials.refreshIfExpired();

            return googleCredentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            log.error("Error getting access token from google api: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void readClients() {
        String json = FileReader.readFileToString(emailProperties.getClientSecret());
        inputStream = new ByteArrayInputStream(json.getBytes());
    }
}

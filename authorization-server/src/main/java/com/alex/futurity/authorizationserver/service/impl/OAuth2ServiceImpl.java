package com.alex.futurity.authorizationserver.service.impl;

import com.alex.futurity.authorizationserver.service.OAuth2Service;
import com.alex.futurity.authorizationserver.utils.FileReader;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@Log4j2
public class OAuth2ServiceImpl implements OAuth2Service {
    private final FileReader fileReader;
    private InputStream inputStream;
    @Value("${mail.username}")
    private String username;
    @Value("${mail.client.secret}")
    private String secretPath;

    public OAuth2ServiceImpl(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    @Override
    public String getAccessToken() {
        try {
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(inputStream);
            googleCredentials.refreshIfExpired();

            return googleCredentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            log.error("Error getting access token from google api: {}" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getUsername() {
        return username;
    }

    @PostConstruct
    private void readClients() {
        String json = fileReader.readFileToString(secretPath);
        inputStream = new ByteArrayInputStream(json.getBytes());
    }
}

package com.alex.futurity.authorizationserver.utils;

import com.alex.futurity.authorizationserver.properties.JwtProperties;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class RsaReader {
    @Getter
    private PrivateKey privateKey;
    private final JwtProperties jwtProperties;

    @PostConstruct
    void readPrivateKeyFile() {
        try {
            String privateKeyString = FileReader.readFileToString(jwtProperties.getPrivateKeyPath());
            privateKey = parseKey(privateKeyString);
        } catch (Exception e) {
            String message = "Error parsing private key: " + e.getMessage();
            log.error(message);

            throw new IllegalStateException(message);
        }
    }

    private static PrivateKey parseKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        key = key.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] decodedKey = Base64.getDecoder().decode(key);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");

        return kf.generatePrivate(keySpec);
    }
}

package com.alex.futurity.apigateway.util;

import com.alex.futurity.apigateway.properties.JwtProperties;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class RsaReader {
    @Getter
    private PublicKey publicKey;
    private final JwtProperties jwtProperties;

    @PostConstruct
    private void readPublicKey() {
        try {
            String privateKeyString = FileReader.readFileToString(jwtProperties.getPublicKeyPath());
            publicKey = parseKey(privateKeyString);
        } catch (Exception e) {
            String message = "Error parsing private key: " + e.getMessage();
            log.error(message);

            throw new IllegalStateException(message);
        }
    }

    private PublicKey parseKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        key = key.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] decodedKey = Base64.getDecoder().decode(key);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");

        return kf.generatePublic(keySpec);
    }
}
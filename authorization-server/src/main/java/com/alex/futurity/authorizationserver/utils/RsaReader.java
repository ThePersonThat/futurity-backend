package com.alex.futurity.authorizationserver.utils;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Component
@Log4j2
public class RsaReader {
    @Value("${jwt.key.private.file}")
    private String privatePath;
    private PrivateKey privateKey;
    private final FileReader reader;

    public RsaReader(FileReader fileReader) {
        this.reader = fileReader;
    }

    @PostConstruct
    private void readPrivateKeyFile() {
        try {
            String privateKeyString = reader.readFileToString(privatePath);
            privateKey = parseKey(privateKeyString);
        } catch (Exception e) {
            String message = "Error parsing private key: " + e.getMessage();
            log.error(message);

            throw new IllegalStateException(message);
        }
    }

    private PrivateKey parseKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        key = key.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] decodedKey = Base64.getDecoder().decode(key);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");

        return kf.generatePrivate(keySpec);
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
}

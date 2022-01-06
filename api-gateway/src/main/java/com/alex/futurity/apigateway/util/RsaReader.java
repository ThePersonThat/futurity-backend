package com.alex.futurity.apigateway.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
@Log4j2
public class RsaReader {
    @Value("${jwt.key.public.key}")
    private String publicPath;
    private FileReader reader;
    private PublicKey publicKey;

    public RsaReader(FileReader reader) {
        this.reader = reader;
    }

    @PostConstruct
    private void readPublicKey() {
        try {
            String privateKeyString = reader.readFileToString(publicPath);
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

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
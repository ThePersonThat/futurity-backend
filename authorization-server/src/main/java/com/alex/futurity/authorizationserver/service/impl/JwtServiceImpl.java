package com.alex.futurity.authorizationserver.service.impl;

import com.alex.futurity.authorizationserver.service.JwtService;
import com.alex.futurity.authorizationserver.utils.RsaReader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {
    private final RsaReader reader;
    @Value("${jwt.expired.time}")
    private int expiredTime;

    public JwtServiceImpl(RsaReader reader) {
        this.reader = reader;
    }

    public String generateAccessToken(Long id) {
        Date date = Date.from(LocalDateTime.now().plusMinutes(expiredTime).toInstant(OffsetDateTime.now().getOffset()));

        return Jwts.builder()
                .setSubject(id.toString())
                .setExpiration(date)
                .signWith(SignatureAlgorithm.RS256, reader.getPrivateKey())
                .compact();
    }
}

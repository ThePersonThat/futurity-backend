package com.alex.futurity.apigateway.security;

import com.alex.futurity.apigateway.util.RsaReader;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final RsaReader reader;

    @Override
    public void verifyToken(String token) {
        Jwts.parser()
                .setSigningKey(reader.getPublicKey())
                .parseClaimsJws(token);
    }
}

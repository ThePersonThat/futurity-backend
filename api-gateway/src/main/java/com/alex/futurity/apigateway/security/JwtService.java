package com.alex.futurity.apigateway.security;

import com.alex.futurity.apigateway.util.RsaReader;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JwtService {
    private final RsaReader reader;

    public void verifyToken(String token) {
        parseToken(token);
    }

    public Long getUserIdFromToken(String token) {
        Jws<Claims> claims = parseToken(token);
        String subject = claims.getBody().getSubject();

        return Long.parseLong(subject);
    }

    private Jws<Claims> parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(reader.getPublicKey())
                .parseClaimsJws(token);
    }
}

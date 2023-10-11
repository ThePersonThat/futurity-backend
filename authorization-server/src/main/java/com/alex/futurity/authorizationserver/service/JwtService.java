package com.alex.futurity.authorizationserver.service;

import com.alex.futurity.authorizationserver.dto.JwtRefreshResponseDto;
import com.alex.futurity.authorizationserver.dto.JwtTokenDto;
import com.alex.futurity.authorizationserver.entity.RefreshToken;
import com.alex.futurity.authorizationserver.exception.ClientSideException;
import com.alex.futurity.authorizationserver.properties.JwtProperties;
import com.alex.futurity.authorizationserver.repo.RefreshTokenRepository;
import com.alex.futurity.authorizationserver.utils.DateUtils;
import com.alex.futurity.authorizationserver.utils.RsaReader;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@AllArgsConstructor
public class JwtService {
    private final RsaReader reader;
    private final RefreshTokenRepository refreshTokenRepo;
    private final JwtProperties jwtProperties;


    @Transactional
    public JwtTokenDto refreshAccessToken(JwtTokenDto request) {
        return refreshTokenRepo.findByRefreshToken(request.getToken())
                .filter(this::validateRefreshToken)
                .map(token -> generateAccessToken(token.getUserId()))
                .map(JwtTokenDto::new)
                .orElseThrow(() -> new ClientSideException("Refresh token is not found", HttpStatus.NOT_FOUND));
    }

    public JwtRefreshResponseDto generateAccessAndRefreshTokenPair(Long id) {
        String accessToken = generateAccessToken(id);
        String refreshToken = generateRefreshToken(id);

        return new JwtRefreshResponseDto(accessToken, refreshToken, jwtProperties.getRefreshExpiredTime() * 86400);
    }

    private String generateAccessToken(Long id) {
        Date expirationDate = DateUtils.toDate(DateUtils.now().plusMinutes(jwtProperties.getAccessExpiredTime()));

        return Jwts.builder()
                .setSubject(id.toString())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.RS256, reader.getPrivateKey())
                .compact();
    }

    private String generateRefreshToken(Long id) {
        Date expirationDate = DateUtils.toDate(DateUtils.now().plusDays(jwtProperties.getRefreshExpiredTime()));

        String token = Jwts.builder()
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.RS256, reader.getPrivateKey())
                .compact();

        RefreshToken refreshToken = new RefreshToken(id, token);
        refreshTokenRepo.save(refreshToken);

        return token;
    }

    private boolean validateRefreshToken(RefreshToken token) {
        try {
            Jwts.parser()
                    .setSigningKey(reader.getPrivateKey())
                    .parseClaimsJws(token.getRefreshToken());
        } catch (ExpiredJwtException e) {
            refreshTokenRepo.delete(token);
            throw new ClientSideException("Refresh token is expired", HttpStatus.GONE);
        }

        return true;
    }
}

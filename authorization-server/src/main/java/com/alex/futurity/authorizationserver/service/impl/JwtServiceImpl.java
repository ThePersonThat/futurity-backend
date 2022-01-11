package com.alex.futurity.authorizationserver.service.impl;

import com.alex.futurity.authorizationserver.dto.JwtRefreshResponseDTO;
import com.alex.futurity.authorizationserver.dto.JwtTokenDTO;
import com.alex.futurity.authorizationserver.entity.RefreshToken;
import com.alex.futurity.authorizationserver.exception.ClientSideException;
import com.alex.futurity.authorizationserver.repo.RefreshTokenRepository;
import com.alex.futurity.authorizationserver.service.JwtService;
import com.alex.futurity.authorizationserver.utils.RsaReader;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {
    private final RsaReader reader;
    private final RefreshTokenRepository refreshTokenRepo;
    @Value("${jwt.access.expired.time}")
    private int expiredAccessTime;
    @Value("${jwt.refresh.expired.time}")
    private int expiredRefreshToken;

    public JwtServiceImpl(RsaReader reader, RefreshTokenRepository refreshTokenRepo) {
        this.reader = reader;
        this.refreshTokenRepo = refreshTokenRepo;
    }


    @Override
    @Transactional
    public JwtTokenDTO refreshAccessToken(JwtTokenDTO request) {
        RefreshToken refreshToken = refreshTokenRepo.findByRefreshToken(request.getToken())
                .orElseThrow(() -> new ClientSideException("Refresh token is not found", HttpStatus.NOT_FOUND));

        validateRefreshToken(refreshToken);
        String newAccessToken = generateAccessToken(refreshToken.getUserId());

        return new JwtTokenDTO(newAccessToken);
    }

    @Override
    public JwtRefreshResponseDTO generateAccessAndRefreshTokenPair(Long id) {
        String accessToken = generateAccessToken(id);
        String refreshToken = generateRefreshToken(id);

        return new JwtRefreshResponseDTO(accessToken, refreshToken, expiredRefreshToken * 86400);
    }

    private String generateAccessToken(Long id) {
        Date date = Date.from(LocalDateTime.now().plusMinutes(expiredAccessTime).toInstant(OffsetDateTime.now().getOffset()));

        return Jwts.builder()
                .setSubject(id.toString())
                .setExpiration(date)
                .signWith(SignatureAlgorithm.RS256, reader.getPrivateKey())
                .compact();
    }

    private String generateRefreshToken(Long id) {
        Date date = Date.from(LocalDateTime.now().plusDays(expiredRefreshToken).toInstant(OffsetDateTime.now().getOffset()));

        String token = Jwts.builder()
                .setExpiration(date)
                .signWith(SignatureAlgorithm.RS256, reader.getPrivateKey())
                .compact();

        RefreshToken refreshToken = new RefreshToken(id, token);
        refreshTokenRepo.save(refreshToken);

        return token;
    }

    private void validateRefreshToken(RefreshToken token) {
        try {
            Jwts.parser()
                    .setSigningKey(reader.getPrivateKey())
                    .parseClaimsJws(token.getRefreshToken());
        } catch (ExpiredJwtException e) {
            refreshTokenRepo.delete(token);
            throw new ClientSideException("Refresh token is expired", HttpStatus.GONE);
        }
    }
}

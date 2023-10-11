package com.alex.futurity.apigateway.security;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class JwtFilter implements GatewayFilter {
    private final RouterValidator validator;
    private final JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (validator.isSecured(request)) {
            if (!AuthorizationHeaderHandler.hasAuthorizationHeader(request)) {
                return AuthorizationHeaderHandler.onError(exchange, "The authorization header is missing");
            }

            try {
                String token = AuthorizationHeaderHandler.getTokenFromHeader(request);
                jwtService.verifyToken(token);
            } catch (ExpiredJwtException e) {
                return AuthorizationHeaderHandler.onError(exchange, "The access token is expired");
            } catch (Exception e) {
                return AuthorizationHeaderHandler.onError(exchange, e.getMessage());
            }
        }

        return chain.filter(exchange);
    }
}

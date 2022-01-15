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
    private final AuthorizationHeaderHandler handler;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (validator.isSecured(request)) {
            if (!handler.hasAuthorizationHeader(request)) {
                return handler.onError(exchange, "The authorization header is missing");
            }

            try {
                String token = handler.getTokenFromHeader(request);
                jwtService.verifyToken(token);
            } catch (ExpiredJwtException e) {
                return handler.onError(exchange, "The access token is expired");
            } catch (Exception e) {
                return handler.onError(exchange, e.getMessage());
            }
        }

        return chain.filter(exchange);
    }
}

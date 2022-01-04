package com.alex.futurity.apigateway.security;

import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class JwtFilter implements GatewayFilter {
    private final RouterValidator validator;
    private final JwtService jwtService;
    private final String AUTHORIZATION = "Authorization";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (validator.isSecured(request)) {
            if (!hasAuthorizationHeader(request)) {
                return onError(exchange);
            }

            try {
                String token = getTokenFromHeader(request);
                jwtService.verifyToken(token);
            } catch (Exception e) {
                return onError(exchange);
            }
        }

        return chain.filter(exchange);
    }

    private boolean hasAuthorizationHeader(ServerHttpRequest request) {
        return request.getHeaders().containsKey(AUTHORIZATION);
    }

    private String getTokenFromHeader(ServerHttpRequest request) {
        String token = request.getHeaders().getOrEmpty(AUTHORIZATION).get(0);

        return token.substring(7); // remove the Bearer word
    }

    private Mono<Void> onError(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        return response.setComplete();
    }
}

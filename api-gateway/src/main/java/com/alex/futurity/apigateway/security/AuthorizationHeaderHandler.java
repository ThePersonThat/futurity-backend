package com.alex.futurity.apigateway.security;

import lombok.experimental.UtilityClass;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@UtilityClass
public class AuthorizationHeaderHandler {
    private final String AUTHORIZATION = "Authorization";

    public boolean hasAuthorizationHeader(ServerHttpRequest request) {
        return request.getHeaders().containsKey(AUTHORIZATION);
    }

    public String getTokenFromHeader(ServerHttpRequest request) {
        String token = request.getHeaders().getOrEmpty(AUTHORIZATION).get(0);

        return token.substring(7); // remove the Bearer word
    }

    public Mono<Void> onError(ServerWebExchange exchange, String message) {
        String json = String.format("{\"message\": \"%s\"}", message);
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        DataBuffer buffer = response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));

        return response.writeWith(Flux.just(buffer));
    }
}

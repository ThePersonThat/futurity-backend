package com.alex.futurity.apigateway.security;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouterValidator {
    private final List<String> endPoints = List.of(
        "user"
    );

    public boolean isSecured(ServerHttpRequest request) {
        String path = request.getURI().getPath();

        return endPoints.stream().anyMatch(path::contains);
    }
}

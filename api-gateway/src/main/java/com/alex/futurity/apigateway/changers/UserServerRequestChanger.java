package com.alex.futurity.apigateway.changers;

import com.alex.futurity.apigateway.security.AuthorizationHeaderHandler;
import com.alex.futurity.apigateway.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class UserServerRequestChanger implements GatewayFilter {
    private final JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String token = AuthorizationHeaderHandler.getTokenFromHeader(request);
        Long id = jwtService.getUserIdFromToken(token);
        String path = insertIdToPath(request.getPath().value(), id);

        ServerHttpRequest newRequest = request.mutate().path(path).build();
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();

        return chain.filter(newExchange);
    }

    private String insertIdToPath(String path, Long id) {
        int index = path.indexOf("/", 1);
        String begin = path.substring(0, index + 1);
        String end = path.substring(index);

        return begin + id.toString()  + end;
    }
}

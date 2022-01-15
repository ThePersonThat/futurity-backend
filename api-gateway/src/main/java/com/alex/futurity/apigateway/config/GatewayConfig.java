package com.alex.futurity.apigateway.config;

import com.alex.futurity.apigateway.security.JwtFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Value("${authorization-server}")
    private String authorizationServer;
    private final JwtFilter filter;

    public GatewayConfig(JwtFilter filter) {
        this.filter = filter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/auth/**")
                        .filters(rw -> rw.rewritePath("/auth/(?<segment>.*)", "/${segment}"))
                        .uri(authorizationServer))
                .route(r -> r.path("/secured/**")
                        .filters(f -> {
                            f.filter(filter);
                            f.rewritePath("/secured/(?<segment>.*)", "/${segment}");
                            return f;
                        })
                        .uri("http://localhost:8085/"))
                .build();
    }
}

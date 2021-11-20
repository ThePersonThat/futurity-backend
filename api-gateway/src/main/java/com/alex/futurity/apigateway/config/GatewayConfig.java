package com.alex.futurity.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Value("${authorization-server}")
    private String authorizationServer;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/auth/**")
                        .filters(rw -> rw.rewritePath("/auth/(?<segment>.*)", "/${segment}"))
                        .uri(authorizationServer))
                .build();
    }
}

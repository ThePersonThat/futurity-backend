package com.alex.futurity.apigateway.config;

import com.alex.futurity.apigateway.changers.UserServerRequestChanger;
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
    @Value("${user-server}")
    private String userServer;
    private final JwtFilter filter;
    private final UserServerRequestChanger requestChanger;

    public GatewayConfig(JwtFilter filter, UserServerRequestChanger requestChanger) {
        this.filter = filter;
        this.requestChanger = requestChanger;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/auth/**")
                        .filters(rw -> rw.rewritePath("/auth/(?<segment>.*)", "/${segment}"))
                        .uri(authorizationServer))
                .route(r -> r.path("/user/**")
                        .filters(f -> {
                            f.filter(filter);
                            f.filter(requestChanger);
                            f.rewritePath("/user/(?<segment>.*)", "/${segment}/");
                            return f;
                        })
                        .uri(userServer))
                .build();
    }
}

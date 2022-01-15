package com.alex.futurity.apigateway.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class JwtFilterTest {
    @Mock
    private RouterValidator validator;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private JwtFilter filter;
    @Mock
    private ServerWebExchange exchange;
    @Mock
    private ServerHttpRequest request;
    @Mock
    private GatewayFilterChain chain;
    @Mock
    private HttpHeaders headers;

    @Test
    @DisplayName("Should not validate the request")
    void testFilterIfRequestIsNotSecured() {
        when(exchange.getRequest()).thenReturn(request);
        when(validator.isSecured(any())).thenReturn(false);

        filter.filter(exchange, chain);

        verify(chain).filter(any());
    }

    @Test
    @DisplayName("Should return error if a request does not have the authorization header")
    void testFilterIfRequestDoesNotHaveAuthorizationHeader() {
        ServerHttpResponse response = new MockServerHttpResponse();
        when(exchange.getRequest()).thenReturn(request);
        when(exchange.getResponse()).thenReturn(response);
        when(validator.isSecured(any())).thenReturn(true);
        when(request.getHeaders()).thenReturn(headers);

        filter.filter(exchange, chain);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
    }

    @Test
    @DisplayName("Should return error if a request has the invalid token")
    void testFilterIfRequestHasInvalidToken() {
        String token = "someMockToken";
        ServerHttpResponse response = new MockServerHttpResponse();
        when(exchange.getRequest()).thenReturn(request);
        when(exchange.getResponse()).thenReturn(response);
        when(validator.isSecured(any())).thenReturn(true);
        when(request.getHeaders()).thenReturn(headers);
        when(headers.containsKey(anyString())).thenReturn(true);
        when(headers.getOrEmpty(anyString())).thenReturn(List.of(token));
        doThrow(new RuntimeException()).when(jwtService).verifyToken(anyString());

        filter.filter(exchange, chain);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
    }
}
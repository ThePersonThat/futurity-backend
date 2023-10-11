package com.alex.futurity.authorizationserver.config;

import com.alex.futurity.authorizationserver.exception.ClientSideException;
import com.alex.futurity.authorizationserver.exception.ErrorMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.support.AbstractFormWriter;
import org.springframework.cloud.openfeign.support.JsonFormWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.io.InputStream;

@Configuration
public class FeignConfig {
    @Bean
    public AbstractFormWriter jsonFormWriter() {
        return new JsonFormWriter();
    }

    @Bean
    public ErrorDecoder errorDecoder(ObjectMapper objectMapper) {
        return new CustomErrorDecoder(objectMapper);
    }

    @Slf4j
    @AllArgsConstructor
    private static class CustomErrorDecoder implements ErrorDecoder {
        private static final ErrorDecoder DEFAULT = new Default();

        private final ObjectMapper objectMapper;

        @Override
        public Exception decode(String s, Response response) {
            if (response.status() == HttpStatus.NOT_FOUND.value()) {
                return new ClientSideException(getResponse(response), HttpStatus.NOT_FOUND);
            }

            return DEFAULT.decode(s, response);
        }

        @SneakyThrows
        private String getResponse(Response response) {
            InputStream stream = response.body().asInputStream();

            return objectMapper.readValue(new String(stream.readAllBytes()), ErrorMessage.class)
                    .getMessage();
        }
    }

}

package com.alex.futurity.authorizationserver.utils;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
@Log4j2
public class HttpHelper {
    private final RestTemplate restTemplate;

    public HttpEntity<MultiValueMap<String, Object>> buildMultiPartHttpEntity(Map<String, List<Object>> fields) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>(fields);

        return new HttpEntity<>(body, headers);
    }

    public <T> T doPost(String url, Object dto, Class<T> responseType) {
        try {
            ResponseEntity<T> response = restTemplate.postForEntity(url, dto, responseType);

            return response.getBody();
        } catch (ResourceAccessException e) {
            log.error("Error getting data for url \"{}\": {}", url, e.getMessage());
            throw new IllegalStateException("Something went wrong. Try again after a while");
        }
    }

    public <T> T doGet(String url, Class<T> responseType) {
        try {
            ResponseEntity<T> response = restTemplate.getForEntity(url, responseType);

            return response.getBody();
        } catch (ResourceAccessException e) {
            log.error("Error getting data for url \"{}\": {}", url, e.getMessage());
            throw new IllegalStateException("Something went wrong. Try again after a while");
        }
    }

    public <T> T doGet(String url, Class<T> responseType, String authHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + authHeader);
            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), responseType);
            return response.getBody();
        } catch (ResourceAccessException e) {
            log.error("Error getting data for url \"{}\": {}", url, e.getMessage());
            throw new IllegalStateException("Something went wrong. Try again after a while");
        }
    }
}

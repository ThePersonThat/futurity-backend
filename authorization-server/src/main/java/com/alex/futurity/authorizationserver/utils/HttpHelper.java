package com.alex.futurity.authorizationserver.utils;

import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

@Component
@Scope("prototype")
public class HttpHelper {

    public HttpEntity<MultiValueMap<String, Object>> buildMultiPartHttpEntity(Map<String, List<Object>> fields) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>(fields);

        return new HttpEntity<>(body, headers);
    }
}

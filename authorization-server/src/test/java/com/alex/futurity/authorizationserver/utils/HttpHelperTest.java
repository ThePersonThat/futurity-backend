package com.alex.futurity.authorizationserver.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class HttpHelperTest {
    private HttpHelper helper;

    @BeforeEach
    private void init() {
        helper = new HttpHelper();
    }

    @Test
    @DisplayName("Should build an http entity with setted field")
    void testBuildMultiPartHttpEntity() {
        String key = "key";
        List<Object> value = List.of("value");
        Map<String, List<Object>> values = Map.of(key, value);
        HttpEntity<MultiValueMap<String, Object>> entity = helper.buildMultiPartHttpEntity(values);

        assertThat(entity.getBody().get(key)).isEqualTo(value);
        assertThat(entity.getHeaders().get("Content-Type"))
                .isEqualTo(List.of(MediaType.MULTIPART_FORM_DATA.toString()));
    }
}
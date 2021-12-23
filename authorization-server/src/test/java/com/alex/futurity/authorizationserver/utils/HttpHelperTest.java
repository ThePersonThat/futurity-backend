package com.alex.futurity.authorizationserver.utils;

import com.alex.futurity.authorizationserver.domain.LoginDomain;
import com.alex.futurity.authorizationserver.dto.LoginRequestDTO;
import com.alex.futurity.authorizationserver.exception.ErrorMessage;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HttpHelperTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private HttpHelper helper;
    private static LogCaptor logCaptor;

    @BeforeAll
    public static void setUpLogCaptor() {
        logCaptor = LogCaptor.forClass(HttpHelper.class);
    }

    @AfterEach
    public void clearLogs() {
        logCaptor.clearLogs();
    }

    @AfterAll
    public static void tearDown() {
        logCaptor.close();
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

    @Test
    @DisplayName("Should do post")
    void testDoPost() {
        String responseExpected = "Success";
        String mockUrl = "mockUrl";
        Object dto = new Object();

        when(restTemplate.postForEntity(anyString(), any(), any()))
                .thenReturn(ResponseEntity.ok().body(responseExpected));

        String response = helper.doPost("mockUrl", dto, String.class);

        assertThat(response).isEqualTo(responseExpected);
        verify(restTemplate).postForEntity(eq(mockUrl), eq(dto), eq(String.class));
    }

    @Test
    @DisplayName("Should do post")
    void testDoGet() {
        String responseExpected = "Success";
        String mockUrl = "mockUrl";

        when(restTemplate.getForEntity(anyString(), any()))
                .thenReturn(ResponseEntity.ok().body(responseExpected));

        String response = helper.doGet("mockUrl", String.class);

        assertThat(response).isEqualTo(responseExpected);
        verify(restTemplate).getForEntity(eq(mockUrl), eq(String.class));
    }

    @Test
    @DisplayName("Should throw an IllegalStateException with error message if can't access to the server")
    void testDoPostWithAccessingError() {
        String url = "fakeUrl";
        String errorMessage = "Request timeout";
        when(restTemplate.postForEntity(anyString(), any(), any()))
                .thenThrow(new ResourceAccessException(errorMessage));

        assertThatThrownBy(() -> helper.doPost(url, new Object(), String.class))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Something went wrong. Try again after a while");

        assertThat(logCaptor.getLogs())
                .hasSize(1)
                .contains(String.format("Error getting data for url \"%s\": %s", url, errorMessage));
    }

    @Test
    @DisplayName("Should throw an IllegalStateException with error message if can't access to the server")
    void testDoGetWithAccessingError() {
        String url = "fakeUrl";
        String errorMessage = "Request timeout";
        when(restTemplate.getForEntity(anyString(), any()))
                .thenThrow(new ResourceAccessException(errorMessage));

        assertThatThrownBy(() -> helper.doGet(url, String.class))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Something went wrong. Try again after a while");

        assertThat(logCaptor.getLogs())
                .hasSize(1)
                .contains(String.format("Error getting data for url \"%s\": %s", url, errorMessage));
    }
}
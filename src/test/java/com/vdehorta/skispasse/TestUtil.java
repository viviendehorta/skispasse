package com.vdehorta.skispasse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * Contains methods and object useful for testing.
 */
public final class TestUtil {

    private TestUtil() {
    }

    public static HttpEntity<Object> buildJsonEntity(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }
}

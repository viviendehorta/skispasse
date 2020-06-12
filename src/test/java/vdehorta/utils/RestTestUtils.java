package vdehorta.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * Utility class for testing REST controllers.
 */
public final class RestTestUtils {

    public static HttpEntity<MultiValueMap<String, Object>> createSingleFileMultipartHttpEntity(String contentDispositionNameEntry, String filename, Map<String, Object> partsByName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setContentDispositionFormData("videoFile", filename);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        for (Map.Entry<String, Object> partByNameEntry : partsByName.entrySet()) {
            body.add(partByNameEntry.getKey(), partByNameEntry.getValue());
        }
        return new HttpEntity<>(body, headers);
    }

    private RestTestUtils() {}
}

package vdehorta.web.rest.util;

import org.springframework.http.HttpHeaders;

/**
 * Utility class for HTTP headers creation.
 */
public class HeaderUtil {

    private HeaderUtil() {
    }

    /**
     * <p>createAlert.</p>
     *
     * @param applicationName a {@link java.lang.String} object.
     * @param message a {@link java.lang.String} object.
     * @return a {@link org.springframework.http.HttpHeaders} object.
     */
    public static HttpHeaders createAlertHeaders(String applicationName, String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + applicationName + "-alert", message);
        return headers;
    }
}

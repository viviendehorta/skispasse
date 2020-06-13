package vdehorta.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

/**
 * Utility class for testing REST controllers.
 */
public final class JsonTestUtils {

    private static final ObjectMapper mapper = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    /**
     * Convert an object to JSON byte array.
     *
     * @param object the object to convert.
     * @return the JSON byte array.
     * @throws IOException
     */
    public static byte[] toJsonBytes(Object object) throws IOException {
        return mapper.writeValueAsBytes(object);
    }

    /**
     * Convert an object to JSON String.
     *
     * @param object the object to convert.
     * @return the JSON String value.
     * @throws IOException
     */
    public static String toJsonString(Object object) throws IOException {
        return mapper.writeValueAsString(object);
    }


    private JsonTestUtils() {}
}

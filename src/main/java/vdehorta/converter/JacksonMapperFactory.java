package vdehorta.converter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonMapperFactory {

    private static ObjectMapper objectMapper;

    private JacksonMapperFactory() {
    }

    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }
}

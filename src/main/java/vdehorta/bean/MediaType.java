package vdehorta.bean;

import java.util.Optional;

public enum MediaType {
    VIDEO,
    PHOTO;

    public static Optional<MediaType> getByValue(String value) {
        for (MediaType mediaType : values()) {
            if (mediaType.name().equals(value)) {
                return Optional.of(mediaType);
            }
        }
        return Optional.empty();
    }
}

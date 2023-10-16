package com.vdehorta.skispasse.model.enums;

public enum MediaType {
    VIDEO,
    PHOTO;

    public static MediaType getByValue(String value) {
        for (MediaType mediaType : values()) {
            if (mediaType.name().equals(value)) {
                return mediaType;
            }
        }
        throw new IllegalArgumentException("\"" + value + "\" is not a valid media type value !");
    }
}

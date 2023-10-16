package com.vdehorta.skispasse.model.enums;

public enum ContentType {

    MP4("video/mp4", MediaType.VIDEO),
    OGG("video/ogg", MediaType.VIDEO),
    WEBM("video/webm", MediaType.VIDEO),
    PNG("image/png", MediaType.PHOTO),
    JPEG("image/jpeg", MediaType.PHOTO);

    private final String contentTypeValue;
    private final MediaType mediaType;

    ContentType(String contentTypeValue, MediaType mediaType) {
        this.contentTypeValue = contentTypeValue;
        this.mediaType = mediaType;
    }

    public static ContentType getByContentType(String value) {
        for (ContentType enumValue : values()) {
            if (enumValue.getContentTypeValue().equals(value)) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException("\"" + value + "\" is not a valid content type value !");
    }

    public String getContentTypeValue() {
        return contentTypeValue;
    }

    public MediaType getMediaType() {
        return mediaType;
    }
}

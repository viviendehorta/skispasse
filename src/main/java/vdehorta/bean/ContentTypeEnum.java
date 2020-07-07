package vdehorta.bean;

import java.util.Optional;

public enum ContentTypeEnum {

    MP4("video/mp4", "MP4"),
    OGG("video/ogg", "OGG"),
    WEBM("video/webm", "WEBM");

    private String contentType;
    private String extension;

    ContentTypeEnum(String contentType, String extension) {
        this.contentType = contentType;
        this.extension = extension;
    }

    public static Optional<ContentTypeEnum> getByContentType(String contentType) {
        for (ContentTypeEnum contentTypeEnum : values()) {
            if (contentTypeEnum.getContentType().equals(contentType)) {
                return Optional.of(contentTypeEnum);
            }
        }
        return Optional.empty();
    }

    public String getContentType() {
        return contentType;
    }

    public String getExtension() {
        return extension;
    }
}

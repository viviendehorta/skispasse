package vdehorta.bean;

import java.util.Optional;

public enum ContentTypeEnum {

    MP4("video/mp4", "MP4"),
    THREE_GP("video/3gpp", "3GP");

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

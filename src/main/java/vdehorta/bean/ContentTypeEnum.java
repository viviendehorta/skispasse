package vdehorta.bean;

import java.util.Optional;

import static vdehorta.bean.MediaType.VIDEO;
import static vdehorta.bean.MediaType.PHOTO;

public enum ContentTypeEnum {

    MP4("video/mp4", VIDEO),
    OGG("video/ogg", VIDEO),
    WEBM("video/webm", VIDEO),
    PNG("image/png", PHOTO);

    private String contentType;
    private MediaType mediaType;

    ContentTypeEnum(String contentType, MediaType mediaType) {
        this.contentType = contentType;
        this.mediaType = mediaType;
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

    public MediaType getMediaType() {
        return mediaType;
    }
}

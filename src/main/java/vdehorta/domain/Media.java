package vdehorta.domain;

import java.util.Objects;

public class Media {

    private String id;
    private String mediaType;
    private String contentType;

    private Media(Builder builder) {
        id = builder.id;
        mediaType = builder.mediaType;
        contentType = builder.contentType;
    }

    public String getId() {
        return id;
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Media media = (Media) o;
        return Objects.equals(id, media.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static final class Builder {
        private String id;
        private String mediaType;
        private String contentType;

        public Builder() {
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder mediaType(String val) {
            mediaType = val;
            return this;
        }

        public Builder contentType(String val) {
            contentType = val;
            return this;
        }

        public Media build() {
            return new Media(this);
        }
    }
}

package vdehorta.bean.dto;

import vdehorta.bean.ContentTypeEnum;
import vdehorta.bean.MediaType;

public class MediaDto {

//    @JsonSerialize(using = MediaTypeSerializer.class)
    private MediaType type;

//    @JsonSerialize(using = ContentTypeEnumSerializer.class)
    private ContentTypeEnum contentType;

    private MediaDto(Builder builder) {
        setType(builder.type);
        setContentType(builder.contentType);
    }

    public MediaDto() {
    }

    public MediaType getType() {
        return type;
    }

    public void setType(MediaType type) {
        this.type = type;
    }

    public ContentTypeEnum getContentType() {
        return contentType;
    }

    public void setContentType(ContentTypeEnum contentType) {
        this.contentType = contentType;
    }


    public static final class Builder {
        private MediaType type;
        private ContentTypeEnum contentType;

        public Builder() {
        }

        public Builder type(MediaType val) {
            type = val;
            return this;
        }

        public Builder contentType(ContentTypeEnum val) {
            contentType = val;
            return this;
        }

        public MediaDto build() {
            return new MediaDto(this);
        }
    }
}

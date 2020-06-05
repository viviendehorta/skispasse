package vdehorta.bean;

import java.io.IOException;
import java.io.InputStream;

/**
 * Represent a file in memory like an newly uploaded file received by servlet.
 * This is not a DTO, do not try to serialize it as it contains an opened InputStream
 */
public class InMemoryFile {

    private InputStream inputStream;
    private long sizeInBytes;
    private String contentType;
    private String originalFilename; //Name of the original file if there was one (can be null)

    private InMemoryFile(Builder builder) {
        originalFilename = builder.originalFilename;
        inputStream = builder.inputStream;
        sizeInBytes = builder.sizeInBytes;
        contentType = builder.contentType;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public long getSizeInBytes() {
        return sizeInBytes;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public String getContentType() {
        return contentType;
    }

    public void closeStream() {
        try {
            this.inputStream.close();
        } catch (IOException e) {}
    }


    public static final class Builder {
        private InputStream inputStream;
        private long sizeInBytes;
        private String contentType;
        private String originalFilename;

        public Builder() {
        }

        public Builder inputStream(InputStream val) {
            inputStream = val;
            return this;
        }

        public Builder sizeInBytes(long val) {
            sizeInBytes = val;
            return this;
        }

        public Builder contentType(String val) {
            contentType = val;
            return this;
        }

        public Builder originalFilename(String val) {
            originalFilename = val;
            return this;
        }

        public InMemoryFile build() {
            return new InMemoryFile(this);
        }
    }
}

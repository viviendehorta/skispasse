package vdehorta.service.errors;

public class UnsupportedFileContentTypeException extends RuntimeException {

    private String contentType;

    public UnsupportedFileContentTypeException(String contentType) {
        super("File content type '" + contentType + "' is not supported!");
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }
}

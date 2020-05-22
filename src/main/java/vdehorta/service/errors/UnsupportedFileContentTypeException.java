package vdehorta.service.errors;

public class UnsupportedFileContentTypeException extends RuntimeException {

    public UnsupportedFileContentTypeException(String contentType) {
        super(String.format("File content type '{}' is not supported!", contentType));
    }
}

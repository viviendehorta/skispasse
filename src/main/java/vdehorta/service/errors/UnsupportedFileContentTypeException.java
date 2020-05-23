package vdehorta.service.errors;

public class UnsupportedFileContentTypeException extends RuntimeException {

    public UnsupportedFileContentTypeException(String contentType) {
        super("File content type '" + contentType + "' is not supported!");
    }
}

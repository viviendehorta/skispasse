package vdehorta.service.errors;

public class UnreadableFileContentException extends RuntimeException {

    public UnreadableFileContentException(String filename) {
        super("Cannot read content of file " + filename + " !");
    }

    public UnreadableFileContentException(String filename, Throwable throwable) {
        super("Cannot read content of file " + filename + " !", throwable);
    }
}

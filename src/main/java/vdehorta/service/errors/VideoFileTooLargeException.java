package vdehorta.service.errors;

public class VideoFileTooLargeException extends RuntimeException {

    public VideoFileTooLargeException(long maxSize) {
        super("Video file size can't exceed " + maxSize + " bytes!");
    }
}

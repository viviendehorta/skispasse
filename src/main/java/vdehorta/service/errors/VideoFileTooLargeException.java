package vdehorta.service.errors;

public class VideoFileTooLargeException extends RuntimeException {

    public VideoFileTooLargeException(long maxSize) {
        super(String.format("Video file size can't exceed {} bytes!", maxSize));
    }
}

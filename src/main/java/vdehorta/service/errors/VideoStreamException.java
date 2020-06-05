package vdehorta.service.errors;

public class VideoStreamException extends RuntimeException {

    public VideoStreamException(String id, Throwable e) {
        super("Error while trying to get stream on video with id '" + id + "' !", e);
    }
}

package vdehorta.service.errors;

public class VideoNotFoundException extends DomainEntityNotFoundException {

    public VideoNotFoundException(String videoId) {
        super("Video", videoId);
    }
}

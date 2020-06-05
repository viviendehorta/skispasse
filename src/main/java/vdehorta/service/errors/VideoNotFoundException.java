package vdehorta.service.errors;

public class VideoNotFoundException extends RuntimeException {

    private String videoId;

    public VideoNotFoundException(String videoId) {
        super("Video with id '" + videoId + "' was not found!");
        this.videoId = videoId;
    }

    public String getVideoId() {
        return videoId;
    }
}

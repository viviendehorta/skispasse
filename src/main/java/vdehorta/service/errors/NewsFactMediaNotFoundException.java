package vdehorta.service.errors;

public class NewsFactMediaNotFoundException extends RuntimeException {

    private String newsFactId;
    private String mediaId;

    public NewsFactMediaNotFoundException(String newsFactId, String mediaId) {
        super("Media with id '" + mediaId + "' of news fact with id '" + newsFactId + "' was not found!");
        this.newsFactId = newsFactId;
        this.mediaId = mediaId;
    }

    public String getNewsFactId() {
        return newsFactId;
    }

    public String getMediaId() {
        return mediaId;
    }
}

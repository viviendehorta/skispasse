package vdehorta.service.errors;

public class NewsFactMediaAccessException extends RuntimeException {

    private String newsFactId;
    private String mediaId;

    public NewsFactMediaAccessException(String newsFactId, String mediaId) {
        super("Error while trying to access to media with id '" + mediaId + "' of news fact with id '" + newsFactId + "' was not found!");
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

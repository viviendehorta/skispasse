package vdehorta.service.errors;

public class NewsFactVideoNotFoundException extends RuntimeException {

    private String newsFactId;

    public NewsFactVideoNotFoundException(String newsFactId) {
        super("Video with id '" + newsFactId + "' was not found!");
        this.newsFactId = newsFactId;
    }

    public String getNewsFactId() {
        return newsFactId;
    }
}

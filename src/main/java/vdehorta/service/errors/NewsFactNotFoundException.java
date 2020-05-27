package vdehorta.service.errors;

public class NewsFactNotFoundException extends RuntimeException {

    private String newsFactId;

    public NewsFactNotFoundException(String newsFactId) {
        super("News fact with id '" + newsFactId + "' was not found!");
        this.newsFactId = newsFactId;
    }

    public String getNewsFactId() {
        return newsFactId;
    }
}

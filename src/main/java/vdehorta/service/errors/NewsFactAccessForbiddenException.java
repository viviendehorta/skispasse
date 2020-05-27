package vdehorta.service.errors;

public class NewsFactAccessForbiddenException extends RuntimeException {

    private String newsFactId;

    public NewsFactAccessForbiddenException(String newsFactId) {
        super("Access to news fact with id '" + newsFactId + "' is forbidden!");
        this.newsFactId = newsFactId;
    }

    public String getNewsFactId() {
        return newsFactId;
    }
}

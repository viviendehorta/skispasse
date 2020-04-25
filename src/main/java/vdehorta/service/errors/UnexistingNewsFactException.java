package vdehorta.service.errors;

public class UnexistingNewsFactException extends RuntimeException {

    public UnexistingNewsFactException(String newsFactId) {
        super("News fact with id '" + newsFactId + "' doesn't exist");
    }
}

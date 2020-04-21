package vdehorta.service.errors;

public class UnexistingNewsFactException extends RuntimeException {

    public UnexistingNewsFactException(Long newsFactId) {
        super("News fact with id'" + newsFactId + "'doesn't exist");
    }
}

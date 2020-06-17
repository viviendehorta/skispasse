package vdehorta.service.errors;

public class NewsFactNotFoundException extends DomainEntityNotFoundException {

    public NewsFactNotFoundException(String newsFactId) {
        super("News fact", newsFactId);
    }
}

package vdehorta.web.rest.errors;

public class NewsFactNotFoundAlertException extends NotFoundAlertException {

    private static final long serialVersionUID = 1L;

    public NewsFactNotFoundAlertException(String newsFactId) {
        super("News fact with id '" + newsFactId + "' was not found!");
    }
}

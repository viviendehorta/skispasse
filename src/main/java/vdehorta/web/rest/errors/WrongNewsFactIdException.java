package vdehorta.web.rest.errors;

public class WrongNewsFactIdException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public WrongNewsFactIdException() {
        super("Wrong news fact id!", "news-fact", "wrongNewsFactId");
    }
}

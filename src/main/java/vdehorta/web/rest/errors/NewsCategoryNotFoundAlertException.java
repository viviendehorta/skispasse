package vdehorta.web.rest.errors;

public class NewsCategoryNotFoundAlertException extends NotFoundAlertException {

    private static final long serialVersionUID = 1L;

    public NewsCategoryNotFoundAlertException(String newsCategoryId) {
        super("News category with id '" + newsCategoryId + "' was not found!");
    }
}

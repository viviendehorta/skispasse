package vdehorta.service.errors;

public class NewsCategoryNotFoundException extends DomainEntityNotFoundException {

    public NewsCategoryNotFoundException(String newsCategoryId) {
        super("News category", newsCategoryId);
    }
}

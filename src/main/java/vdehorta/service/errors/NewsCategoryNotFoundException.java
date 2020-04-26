package vdehorta.service.errors;

public class NewsCategoryNotFoundException extends RuntimeException {

    public NewsCategoryNotFoundException(String newsCategoryId) {
        super("News category with id '" + newsCategoryId + "' doesn't exist");
    }
}

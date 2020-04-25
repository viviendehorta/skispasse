package vdehorta.service.errors;

public class UnexistingNewsCategoryException extends RuntimeException {

    public UnexistingNewsCategoryException(String newsCategoryId) {
        super("News category with id '" + newsCategoryId + "' doesn't exist");
    }
}

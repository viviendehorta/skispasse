package vdehorta.service.errors;

public class WrongNewsCategoryIdException extends RuntimeException {

    public WrongNewsCategoryIdException(String categoryId) {
        super("Wrong news category id '" + categoryId + "' !");
    }
}

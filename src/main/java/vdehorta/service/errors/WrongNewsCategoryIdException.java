package vdehorta.service.errors;

public class WrongNewsCategoryIdException extends RuntimeException {

    public WrongNewsCategoryIdException(String categoryId) {
        super(String.format("Wrong news category id '{}' !", categoryId));
    }
}

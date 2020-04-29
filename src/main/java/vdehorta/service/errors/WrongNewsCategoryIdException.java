package vdehorta.service.errors;

public class WrongNewsCategoryIdException extends RuntimeException {

    public WrongNewsCategoryIdException() {
        super("Wrong news category id!");
    }
}

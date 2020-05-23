package vdehorta.service.errors;

public class WrongNewsFactIdException extends RuntimeException {

    public WrongNewsFactIdException(String newsFactId) {
        super("Wrong news fact id '" + newsFactId + "'!");
    }
}

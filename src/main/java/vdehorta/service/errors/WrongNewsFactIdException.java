package vdehorta.service.errors;

public class WrongNewsFactIdException extends RuntimeException {

    public WrongNewsFactIdException() {
        super("Wrong news fact id!");
    }
}

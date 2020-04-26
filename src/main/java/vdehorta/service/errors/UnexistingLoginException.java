package vdehorta.service.errors;

public class UnexistingLoginException extends RuntimeException {

    public UnexistingLoginException() {
        super("Unexisting user login!");
    }

}

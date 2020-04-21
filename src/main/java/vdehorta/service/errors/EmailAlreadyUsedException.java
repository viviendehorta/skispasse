package vdehorta.service.errors;

public class EmailAlreadyUsedException extends RuntimeException {

    public EmailAlreadyUsedException() {
        super("Email is already in use!");
    }

}

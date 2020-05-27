package vdehorta.service.errors;

public class EmailAlreadyUsedException extends RuntimeException {

    public EmailAlreadyUsedException(String email) {
        super("Email '" + email + "' is already used!");
    }

}

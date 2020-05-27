package vdehorta.service.errors;

public class LoginAlreadyUsedException extends RuntimeException {

    public LoginAlreadyUsedException(String login) {
        super("User login '" + login + "' is already used!");
    }

}

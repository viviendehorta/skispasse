package vdehorta.service.errors;

public class LoginAlreadyUsedException extends RuntimeException {

    public LoginAlreadyUsedException() {
        super("User login already used!");
    }

}

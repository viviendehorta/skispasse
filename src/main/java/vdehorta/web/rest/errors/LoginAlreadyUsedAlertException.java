package vdehorta.web.rest.errors;

public class LoginAlreadyUsedAlertException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public LoginAlreadyUsedAlertException() {
        super("Login name already used!");
    }
}

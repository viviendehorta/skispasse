package vdehorta.web.rest.errors;

public class UnexistingLoginException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public UnexistingLoginException() {
        super("Unexisting user login!", "user", "unexistingLogin");
    }
}

package vdehorta.web.rest.errors;

public class AuthenticationRequiredAlertException extends UnauthorizedAlertException {

    private static final long serialVersionUID = 1L;

    public AuthenticationRequiredAlertException() {
        super("Authentication is required!");
    }
}

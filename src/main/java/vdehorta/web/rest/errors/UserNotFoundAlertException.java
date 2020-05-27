package vdehorta.web.rest.errors;

public class UserNotFoundAlertException extends NotFoundAlertException {

    private static final long serialVersionUID = 1L;

    public UserNotFoundAlertException(String login) {
        super("User with login '" + login + "' could not be found!");
    }
}

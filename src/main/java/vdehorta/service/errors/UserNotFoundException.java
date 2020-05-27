package vdehorta.service.errors;

public class UserNotFoundException extends RuntimeException {

    private String login;

    public UserNotFoundException(String login) {
        super("User with login '" + login + "' could not be found!");
        this.login = login;
    }

    public String getLogin() {
        return login;
    }
}

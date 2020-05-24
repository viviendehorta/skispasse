package vdehorta.service;

import vdehorta.security.RoleEnum;

public class AuthenticationRequiredException extends RuntimeException {

    public AuthenticationRequiredException(RoleEnum requiredRole) {
        super("Authentication with role '" + requiredRole.getValue() + "' is required!");
    }

    public AuthenticationRequiredException() {
        this(RoleEnum.USER);
    }

}

package vdehorta.service;

import vdehorta.security.RoleEnum;

public class AuthenticationRequiredException extends RuntimeException {

    private RoleEnum requiredRole;

    public AuthenticationRequiredException(RoleEnum requiredRole) {
        super("Authentication with role '" + requiredRole.getValue() + "' is required!");
        this.requiredRole = requiredRole;
    }

    public AuthenticationRequiredException() {
        this(RoleEnum.USER);
    }

    public RoleEnum getRequiredRole() {
        return requiredRole;
    }
}

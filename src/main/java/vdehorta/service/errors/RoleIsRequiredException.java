package vdehorta.service.errors;

import vdehorta.security.RoleEnum;

public class RoleIsRequiredException extends RuntimeException {

    private RoleEnum requiredRole;

    public RoleIsRequiredException(RoleEnum requiredRole) {
        super("Role '" + requiredRole.getValue() + "' is required!");
        this.requiredRole = requiredRole;
    }

    public RoleEnum getRequiredRole() {
        return requiredRole;
    }
}

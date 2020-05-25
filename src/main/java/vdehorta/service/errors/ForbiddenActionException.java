package vdehorta.service.errors;

import vdehorta.security.RoleEnum;

public class ForbiddenActionException extends RuntimeException {

    private RoleEnum requiredRole;

    public ForbiddenActionException(RoleEnum requiredRole) {
        super("Role '" + requiredRole.getValue() + "' is required!");
        this.requiredRole = requiredRole;
    }

    public RoleEnum getRequiredRole() {
        return requiredRole;
    }
}

package vdehorta.service.errors;

import vdehorta.security.RoleEnum;

public class MissingRoleException extends RuntimeException {

    private RoleEnum requiredRole;

    public MissingRoleException(RoleEnum requiredRole) {
        super("Role '" + requiredRole.getValue() + "' is required!");
        this.requiredRole = requiredRole;
    }

    public RoleEnum getRequiredRole() {
        return requiredRole;
    }
}

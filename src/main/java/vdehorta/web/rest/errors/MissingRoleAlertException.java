package vdehorta.web.rest.errors;

import com.google.common.collect.ImmutableMap;
import vdehorta.security.RoleEnum;

import java.util.Map;

import static vdehorta.security.RoleEnum.*;

public class MissingRoleAlertException extends ForbiddenAlertException {

    private static final long serialVersionUID = 1L;

    private static final Map<RoleEnum, String> roleDictionary = ImmutableMap.of(ADMIN, "administrator", CONTRIBUTOR, "contributor", USER, "user");

    private RoleEnum requiredRole;

    public MissingRoleAlertException(RoleEnum requiredRole) {
        super("Role '" + getReadableRole(requiredRole) + "' is required!");
        this.requiredRole = requiredRole;
    }

    public RoleEnum getRequiredRole() {
        return requiredRole;
    }

    private static String getReadableRole(RoleEnum requiredRole) {
        return roleDictionary.getOrDefault(requiredRole, requiredRole.getValue());
    }
}

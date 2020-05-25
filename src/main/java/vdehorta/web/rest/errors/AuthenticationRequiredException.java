package vdehorta.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;
import vdehorta.security.RoleEnum;

public class AuthenticationRequiredException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public AuthenticationRequiredException(RoleEnum requiredRole) {
        super(null, "Authentication with role '" + requiredRole + "' is required!", Status.UNAUTHORIZED);
    }
}

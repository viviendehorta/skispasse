package vdehorta.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class UnauthorizedAlertException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public UnauthorizedAlertException(String message) {
        super(null, Status.UNAUTHORIZED.getReasonPhrase(), Status.UNAUTHORIZED, message);
    }
}

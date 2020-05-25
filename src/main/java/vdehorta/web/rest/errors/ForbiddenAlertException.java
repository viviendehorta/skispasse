package vdehorta.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class ForbiddenAlertException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public ForbiddenAlertException(String message) {
        super(null, Status.FORBIDDEN.getReasonPhrase(), Status.FORBIDDEN, message);
    }
}

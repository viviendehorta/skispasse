package vdehorta.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class NotFoundAlertException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public NotFoundAlertException(String message) {
        super(null, Status.NOT_FOUND.getReasonPhrase(), Status.NOT_FOUND, message);
    }
}

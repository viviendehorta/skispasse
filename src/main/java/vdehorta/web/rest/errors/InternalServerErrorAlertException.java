package vdehorta.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class InternalServerErrorAlertException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public InternalServerErrorAlertException(String detail) {
        super(null, Status.INTERNAL_SERVER_ERROR.getReasonPhrase(), Status.INTERNAL_SERVER_ERROR, detail);
    }
}

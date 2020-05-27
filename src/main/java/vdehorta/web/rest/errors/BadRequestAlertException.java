package vdehorta.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class BadRequestAlertException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public BadRequestAlertException(String detail) {
        super(null, Status.BAD_REQUEST.getReasonPhrase(), Status.BAD_REQUEST, detail);
    }
}

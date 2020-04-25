package vdehorta.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class UnexistingNewsFactException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public UnexistingNewsFactException(String newsFactId) {
        super(null, "News fact with id '" + newsFactId + "' doesn't exist", Status.BAD_REQUEST);
    }
}

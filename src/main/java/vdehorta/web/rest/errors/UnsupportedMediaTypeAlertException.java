package vdehorta.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class UnsupportedMediaTypeAlertException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public UnsupportedMediaTypeAlertException(String detail) {
        super(null, Status.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase(), Status.UNSUPPORTED_MEDIA_TYPE, detail);
    }
}

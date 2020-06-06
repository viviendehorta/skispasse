package vdehorta.web.rest.errors;

import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

public class BeanValidationAlertException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public BeanValidationAlertException(List<FieldError> fieldErrors) {
        super(fieldErrors.stream()
                .map(field -> "Value '" + field.getRejectedValue() + "' is invalid for field '" + field.getField() + "'")
                .collect(Collectors.joining(", ", "Some data is invalid : ", "!")));
    }
}

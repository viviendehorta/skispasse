package com.vdehorta.skispasse.web.validator;

import com.vdehorta.skispasse.model.dto.NewsFactDto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class NewsFactDtoValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return NewsFactDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NewsFactDto dto = (NewsFactDto) target;
//        if (StringUtils.isBlank(dto.email())) {
//            errors.rejectValue("email", "email.required", "email is required.");
//        } else if (!EMAIL_VALIDATOR.isValid(dto.email())) {
//            errors.rejectValue("email", "email.invalid", "email has incorrect format.");
//        }
    }
}

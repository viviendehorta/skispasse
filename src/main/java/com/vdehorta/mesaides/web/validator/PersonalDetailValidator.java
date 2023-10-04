package com.vdehorta.mesaides.web.validator;

import com.vdehorta.mesaides.model.dto.PersonalDetailDto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PersonalDetailValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return PersonalDetailDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PersonalDetailDto dto = (PersonalDetailDto) target;
//        if (StringUtils.isBlank(dto.email())) {
//            errors.rejectValue("email", "email.required", "email is required.");
//        } else if (!EMAIL_VALIDATOR.isValid(dto.email())) {
//            errors.rejectValue("email", "email.invalid", "email has incorrect format.");
//        }
    }
}

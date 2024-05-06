package com.devframe.global.common.argumentresolver;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import java.util.Collection;
import java.util.List;

@Component
public class CustomValidator implements Validator {

    private final SpringValidatorAdapter validator;

    public CustomValidator(SpringValidatorAdapter validator) {
        this.validator = validator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        // List 타입을 지원
        return List.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        // Collection으로 캐스팅 후 객체를 순회하여 유효성 검사 수행
        for (Object object : (Collection<?>) target) {
            validator.validate(object, errors);
        }
    }
}
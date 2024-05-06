package com.devframe.global.common.argumentresolver;


import com.devframe.global.aop.ListValid;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ListValidationArgumentResolver implements HandlerMethodArgumentResolver {

    private final CustomValidator customValidator;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isListValid = parameter.hasParameterAnnotation(ListValid.class);
        boolean isCollectionType = Collection.class.isAssignableFrom(parameter.getParameterType());
        return isListValid && isCollectionType;
     }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        System.out.println("parameter = " + parameter);

//        // List로 들어온 객체를 Spring Validation을 이용하여 검증하고 에러가 있을 경우 MethodArgumentNotValidException 예외를 던지기
//        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
//        ServletWebRequest servletWebRequest = new ServletWebRequest(request);
//        WebDataBinderFactory factory = binderFactory;
//        Errors errors = new ServletRequestDataBinder(null).getBindingResult();
//
//        // ListValid 어노테이션을 찾기
//        ListValid listValidAnnotation = parameter.getParameterAnnotation(ListValid.class);
//        // ListValid 어노테이션의 value 값(검증할 그룹) 가져오기
//        Class<?>[] groups = listValidAnnotation.value();
//
//        // 리스트 파라미터 가져오기
//        List<?> list = (List<?>) ServletRequestUtils.getRequiredAttribute(request, parameter.getParameterName());
//
//        // 리스트 안의 객체들을 유효성 검사
//        for (Object obj : list) {
//            customValidator.validate(obj, errors);
//        }
//
//        // 유효성 검사 에러가 있을 경우 MethodArgumentNotValidException 예외를 던지기
//        if (errors.hasErrors()) {
//            throw new MethodArgumentNotValidException(parameter, errors);
//        }

//        return list;
        return true;
    }
}

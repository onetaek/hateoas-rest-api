package com.devframe.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Aspect
@Component
@Slf4j
public class ListValidationAspect {

    @Before("@annotation(com.devframe.global.aop.ListValid)")
    public void validate(JoinPoint joinPoint) {
        System.out.println("joinPoint = " + joinPoint);
        Signature signature = joinPoint.getSignature();
        // List<Object> 형태로 받은 객체를 spring validation을 사용해서 유효성 체크 후
        // 예외가 존재한다면 MethodArgumentNotValidException 예외를 던지기

    }

}

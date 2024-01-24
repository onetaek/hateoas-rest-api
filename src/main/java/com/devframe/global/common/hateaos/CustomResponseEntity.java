package com.devframe.global.common.hateaos;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class CustomResponseEntity<T> extends ResponseEntity<T> {

    public CustomResponseEntity(T body, HttpStatusCode status) {
        super(body, status);
    }

    public static <T> CustomResponseEntity<T> of(T body, HttpStatus httpStatus) {
        return new CustomResponseEntity<>(body, httpStatus);
    }

    public static <T> CustomResponseEntity<T> ok(T data) {
        return of(data, HttpStatus.OK);
    }

    public static <T> CustomResponseEntity<T> created(T data) {
        return of(data, HttpStatus.CREATED);
    }

    public static <T> CustomResponseEntity<T> noContent(T data) {
        return of(data, HttpStatus.NO_CONTENT);
    }
}

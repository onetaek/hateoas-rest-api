package com.devframe.global.common.hateaos;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

public class CustomResponseEntity extends ResponseEntity<CustomResponseBody> {

    public CustomResponseEntity(HttpStatusCode status) {
        super(status);
    }

    public CustomResponseEntity(CustomResponseBody body, HttpStatusCode status) {
        super(body, status);
    }

    public static CustomResponseEntity of(HttpStatus httpStatus) {
        return new CustomResponseEntity(httpStatus);
    }

    public static CustomResponseEntity of(CustomResponseBody body, HttpStatus httpStatus) {
        return new CustomResponseEntity(body, httpStatus);
    }

    public static CustomResponseEntity succeeded(CustomResponseBody data) {
        return of(data, HttpStatus.OK);
    }

    public static CustomResponseEntity created(CustomResponseBody data) {
        return of(data, HttpStatus.CREATED);
    }

    public static CustomResponseEntity error(CustomResponseBody data, HttpStatus httpStatus) {
        return of(data, httpStatus);
    }

    public CustomResponseEntity addLink(LinkProxy linkProxy) {
        Objects.requireNonNull(super.getBody()).addLink(linkProxy);
        return this;
    }

    public CustomResponseEntity addLinks(LinkProxy... linkProxies) {
        Objects.requireNonNull(super.getBody()).addLinks(linkProxies);
        return this;
    }
}

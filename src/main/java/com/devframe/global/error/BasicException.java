package com.devframe.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BasicException extends RuntimeException {

    private final String message;
    private final HttpStatus httpStatus;

    public BasicException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
